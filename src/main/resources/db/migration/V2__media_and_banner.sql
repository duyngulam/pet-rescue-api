-- ============================================================
-- V2: Media refactor + Banner table + Pet rescue case reference
-- - Modify pet_media to reference media_files (single source of truth)
-- - Remove url column from pet_media (build URL from public_id)
-- - Add banners table for landing page management
-- - Add rescue_case_id to pets table
-- ============================================================

-- ── Modify pet_media to reference media_files ────────────────

-- Add media_id column referencing media_files
ALTER TABLE pet_media
ADD COLUMN IF NOT EXISTS media_file_id UUID REFERENCES media_files(media_id);

-- Create index for media_file_id lookup
CREATE INDEX IF NOT EXISTS idx_pet_media_media_file ON pet_media (media_file_id);

-- Drop the url column - URLs are now built from public_id (single source of truth)
ALTER TABLE pet_media DROP COLUMN IF EXISTS url;

-- ── Add rescue_case_id to pets table ─────────────────────────

ALTER TABLE pets
ADD COLUMN IF NOT EXISTS rescue_case_id UUID REFERENCES rescue_cases(case_id);

CREATE INDEX IF NOT EXISTS idx_pet_rescue_case ON pets (rescue_case_id);

-- ── Banners Table ────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS banners (
    banner_id       UUID PRIMARY KEY,
    title           VARCHAR(255),
    subtitle        TEXT,
    media_id        UUID REFERENCES media_files(media_id),
    link_url        TEXT,
    link_type       VARCHAR(50) DEFAULT 'NONE',      -- INTERNAL, EXTERNAL, NONE
    display_order   INTEGER NOT NULL DEFAULT 0,
    start_date      TIMESTAMPTZ,
    end_date        TIMESTAMPTZ,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    target_page     VARCHAR(100) DEFAULT 'HOME',     -- HOME, ADOPTION, RESCUE, etc.
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by      UUID,
    updated_at      TIMESTAMPTZ,
    updated_by      UUID,
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at      TIMESTAMPTZ,
    deleted_by      UUID
);

CREATE INDEX IF NOT EXISTS idx_banners_active ON banners (is_active) WHERE is_deleted = FALSE;
CREATE INDEX IF NOT EXISTS idx_banners_target_page ON banners (target_page);
CREATE INDEX IF NOT EXISTS idx_banners_display_order ON banners (display_order);
CREATE INDEX IF NOT EXISTS idx_banners_dates ON banners (start_date, end_date);

-- ── Media temp status tracking ───────────────────────────────

-- Add status column to media_files for temp upload flow
ALTER TABLE media_files
ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'PERMANENT';

-- Add index for status
CREATE INDEX IF NOT EXISTS idx_media_status ON media_files (status);

COMMENT ON COLUMN media_files.status IS 'TEMP = awaiting confirmation, PERMANENT = confirmed and in use';
