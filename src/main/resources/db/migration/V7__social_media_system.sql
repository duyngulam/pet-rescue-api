-- ============================================================
-- V7: Social Media System — Comments & Likes
-- Event-driven architecture with denormalized counters
-- ============================================================

-- ── Comments Table ──────────────────────────────────────
-- Support 2-level hierarchy: parent comments + child replies
-- parent_comment_id IS NULL = root/parent comment
-- parent_comment_id NOT NULL = reply (child comment)
CREATE TABLE IF NOT EXISTS comments (
    comment_id          UUID PRIMARY KEY,
    post_id             UUID         NOT NULL REFERENCES posts (post_id) ON DELETE CASCADE,
    parent_comment_id   UUID         REFERENCES comments (comment_id) ON DELETE CASCADE,
    author_id           UUID         NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    content             TEXT         NOT NULL,
    
    -- Denormalized counters (synced from Redis via Spring Batch)
    like_count          INTEGER      NOT NULL DEFAULT 0,
    reply_count         INTEGER      NOT NULL DEFAULT 0,  -- Only for parent comments
    
    -- Audit fields
    created_at          TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by          UUID,
    updated_at          TIMESTAMPTZ,
    updated_by          UUID,
    is_deleted          BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at          TIMESTAMPTZ,
    deleted_by          UUID,
    
    -- Max 2-level nesting is enforced by trigger function below
    -- (PostgreSQL CHECK constraints cannot contain subqueries).
    CONSTRAINT check_not_self_parent CHECK (parent_comment_id IS NULL OR parent_comment_id <> comment_id)
);

-- Indexes for query performance
CREATE INDEX IF NOT EXISTS idx_comments_post_id 
    ON comments (post_id, created_at DESC) 
    WHERE is_deleted = FALSE;

CREATE INDEX IF NOT EXISTS idx_comments_parent_id 
    ON comments (parent_comment_id, created_at DESC) 
    WHERE is_deleted = FALSE;

CREATE INDEX IF NOT EXISTS idx_comments_author 
    ON comments (author_id);

-- Index for cursor-based pagination
CREATE INDEX IF NOT EXISTS idx_comments_cursor 
    ON comments (post_id, created_at DESC, comment_id) 
    WHERE is_deleted = FALSE AND parent_comment_id IS NULL;

COMMENT ON TABLE comments IS 'User comments on posts. Supports 2-level hierarchy (parent + child only)';
COMMENT ON COLUMN comments.parent_comment_id IS 'NULL = parent comment, NOT NULL = child reply';
COMMENT ON COLUMN comments.like_count IS 'Denormalized counter synced from Redis';
COMMENT ON COLUMN comments.reply_count IS 'Number of child comments (parent comments only)';

-- Enforce max 2-level nesting (parent + one reply level).
-- A reply cannot target another reply.
CREATE OR REPLACE FUNCTION enforce_comment_nesting_level()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.parent_comment_id IS NULL THEN
        RETURN NEW;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM comments parent
        WHERE parent.comment_id = NEW.parent_comment_id
          AND parent.parent_comment_id IS NOT NULL
    ) THEN
        RAISE EXCEPTION 'Comments support only 2 levels (parent + child)';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_enforce_comment_nesting_level
BEFORE INSERT OR UPDATE OF parent_comment_id ON comments
FOR EACH ROW
EXECUTE FUNCTION enforce_comment_nesting_level();


-- ── Post Likes Table ────────────────────────────────────
-- Track which users liked which posts
-- Composite PK ensures one user can like a post only once (idempotency)
CREATE TABLE IF NOT EXISTS post_likes (
    post_id     UUID        NOT NULL REFERENCES posts (post_id) ON DELETE CASCADE,
    user_id     UUID        NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    
    PRIMARY KEY (post_id, user_id)
);

CREATE INDEX IF NOT EXISTS idx_post_likes_user 
    ON post_likes (user_id);

CREATE INDEX IF NOT EXISTS idx_post_likes_created 
    ON post_likes (post_id, created_at DESC);

COMMENT ON TABLE post_likes IS 'Post likes. Composite PK ensures one user = one like per post';


-- ── Comment Likes Table ─────────────────────────────────
-- Track which users liked which comments
-- Composite PK ensures one user can like a comment only once (idempotency)
CREATE TABLE IF NOT EXISTS comment_likes (
    comment_id  UUID        NOT NULL REFERENCES comments (comment_id) ON DELETE CASCADE,
    user_id     UUID        NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    
    PRIMARY KEY (comment_id, user_id)
);

CREATE INDEX IF NOT EXISTS idx_comment_likes_user 
    ON comment_likes (user_id);

CREATE INDEX IF NOT EXISTS idx_comment_likes_created 
    ON comment_likes (comment_id, created_at DESC);

COMMENT ON TABLE comment_likes IS 'Comment likes. Composite PK ensures one user = one like per comment';


-- ── Update Posts Table ──────────────────────────────────
-- Add denormalized counters for performance
ALTER TABLE posts 
ADD COLUMN IF NOT EXISTS like_count INTEGER NOT NULL DEFAULT 0,
ADD COLUMN IF NOT EXISTS comment_count INTEGER NOT NULL DEFAULT 0;

COMMENT ON COLUMN posts.like_count IS 'Denormalized counter synced from Redis (fast reads)';
COMMENT ON COLUMN posts.comment_count IS 'Total number of comments (parent + child)';

-- Index for posts ordered by popularity
CREATE INDEX IF NOT EXISTS idx_posts_popularity 
    ON posts (like_count DESC, created_at DESC) 
    WHERE is_deleted = FALSE;


-- ── Triggers for Automatic Counter Updates ─────────────
-- Increment/decrement counters on like/unlike
-- These are fallback for DB consistency; primary updates come via events + Redis

-- Post like count trigger
CREATE OR REPLACE FUNCTION update_post_like_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE posts SET like_count = like_count + 1 WHERE post_id = NEW.post_id;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE posts SET like_count = GREATEST(like_count - 1, 0) WHERE post_id = OLD.post_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_post_like_count
AFTER INSERT OR DELETE ON post_likes
FOR EACH ROW EXECUTE FUNCTION update_post_like_count();

-- Comment like count trigger
CREATE OR REPLACE FUNCTION update_comment_like_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE comments SET like_count = like_count + 1 WHERE comment_id = NEW.comment_id;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE comments SET like_count = GREATEST(like_count - 1, 0) WHERE comment_id = OLD.comment_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_comment_like_count
AFTER INSERT OR DELETE ON comment_likes
FOR EACH ROW EXECUTE FUNCTION update_comment_like_count();

-- Post comment count trigger
CREATE OR REPLACE FUNCTION update_post_comment_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE posts SET comment_count = comment_count + 1 WHERE post_id = NEW.post_id;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE posts SET comment_count = GREATEST(comment_count - 1, 0) WHERE post_id = OLD.post_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_post_comment_count
AFTER INSERT OR DELETE ON comments
FOR EACH ROW EXECUTE FUNCTION update_post_comment_count();

-- Parent comment reply count trigger (only for child comments)
CREATE OR REPLACE FUNCTION update_comment_reply_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' AND NEW.parent_comment_id IS NOT NULL THEN
        UPDATE comments SET reply_count = reply_count + 1 WHERE comment_id = NEW.parent_comment_id;
    ELSIF TG_OP = 'DELETE' AND OLD.parent_comment_id IS NOT NULL THEN
        UPDATE comments SET reply_count = GREATEST(reply_count - 1, 0) WHERE comment_id = OLD.parent_comment_id;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_comment_reply_count
AFTER INSERT OR DELETE ON comments
FOR EACH ROW EXECUTE FUNCTION update_comment_reply_count();

COMMENT ON TRIGGER trigger_post_like_count ON post_likes IS 'Auto-increment/decrement post.like_count';
COMMENT ON TRIGGER trigger_comment_like_count ON comment_likes IS 'Auto-increment/decrement comment.like_count';
COMMENT ON TRIGGER trigger_post_comment_count ON comments IS 'Auto-increment/decrement post.comment_count';
COMMENT ON TRIGGER trigger_comment_reply_count ON comments IS 'Auto-increment/decrement parent comment.reply_count';
