-- V6: Password reset tokens
-- Stores one-time tokens for password reset flow

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id          UUID PRIMARY KEY,
    user_id     UUID         NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    token       VARCHAR(512) NOT NULL UNIQUE,
    expires_at  TIMESTAMPTZ  NOT NULL,
    used        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    created_by  UUID,
    updated_at  TIMESTAMPTZ,
    updated_by  UUID,
    is_deleted  BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at  TIMESTAMPTZ,
    deleted_by  UUID
);

-- Index for fast token lookup
CREATE INDEX IF NOT EXISTS idx_password_reset_token ON password_reset_tokens(token) WHERE is_deleted = false;

-- Index for finding latest token by user
CREATE INDEX IF NOT EXISTS idx_password_reset_user_created ON password_reset_tokens(user_id, created_at DESC) WHERE is_deleted = false;
