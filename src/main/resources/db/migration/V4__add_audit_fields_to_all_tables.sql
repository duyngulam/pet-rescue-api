-- V4: Add missing audit fields to users, roles, refresh_tokens, email_verification_tokens tables
-- Ensures all entities follow BaseEntity audit pattern with UUID audit fields
-- Also converts existing VARCHAR(255) audit fields to UUID for consistency

-- Convert existing VARCHAR(255) audit fields in pets table to UUID
ALTER TABLE pets 
    ALTER COLUMN created_by TYPE UUID USING CASE 
        WHEN created_by IS NOT NULL AND created_by ~ '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' 
        THEN created_by::UUID 
        ELSE NULL 
    END,
    ALTER COLUMN updated_by TYPE UUID USING CASE 
        WHEN updated_by IS NOT NULL AND updated_by ~ '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' 
        THEN updated_by::UUID 
        ELSE NULL 
    END,
    ALTER COLUMN deleted_by TYPE UUID USING CASE 
        WHEN deleted_by IS NOT NULL AND deleted_by ~ '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' 
        THEN deleted_by::UUID 
        ELSE NULL 
    END;

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS created_by  UUID,
    ADD COLUMN IF NOT EXISTS updated_by  UUID,
    ADD COLUMN IF NOT EXISTS is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at  TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS deleted_by  UUID;

ALTER TABLE roles
    ADD COLUMN IF NOT EXISTS created_by  UUID,
    ADD COLUMN IF NOT EXISTS updated_at  TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS updated_by  UUID,
    ADD COLUMN IF NOT EXISTS is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at  TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS deleted_by  UUID;

ALTER TABLE refresh_tokens
    ADD COLUMN IF NOT EXISTS created_by  UUID,
    ADD COLUMN IF NOT EXISTS updated_at  TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS updated_by  UUID,
    ADD COLUMN IF NOT EXISTS is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at  TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS deleted_by  UUID;

ALTER TABLE email_verification_tokens
    ADD COLUMN IF NOT EXISTS created_by  UUID,
    ADD COLUMN IF NOT EXISTS updated_at  TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS updated_by  UUID,
    ADD COLUMN IF NOT EXISTS is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at  TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS deleted_by  UUID;
