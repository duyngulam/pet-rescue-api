-- V3: Add missing audit and soft-delete columns to organizations table

ALTER TABLE organizations
    ADD COLUMN IF NOT EXISTS updated_at  TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS updated_by  UUID,
    ADD COLUMN IF NOT EXISTS is_deleted  BOOLEAN     NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS deleted_at  TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS deleted_by  UUID;
