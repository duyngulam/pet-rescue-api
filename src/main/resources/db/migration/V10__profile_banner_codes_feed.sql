-- ============================================================
-- V10: Profile/Banner enhancements + visual codes
-- ============================================================

-- ── Organization: single image URL ──────────────────────────
ALTER TABLE organizations
    ADD COLUMN IF NOT EXISTS image_url TEXT;

-- ── Banner: CTA button text ─────────────────────────────────
ALTER TABLE banners
    ADD COLUMN IF NOT EXISTS button_text VARCHAR(255);

-- ── Visual code columns ─────────────────────────────────────
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS user_code VARCHAR(16);

ALTER TABLE organizations
    ADD COLUMN IF NOT EXISTS organization_code VARCHAR(16);

ALTER TABLE pets
    ADD COLUMN IF NOT EXISTS pet_code VARCHAR(16);

ALTER TABLE rescue_cases
    ADD COLUMN IF NOT EXISTS case_code VARCHAR(16);

ALTER TABLE adoption_applications
    ADD COLUMN IF NOT EXISTS adoption_code VARCHAR(16);

-- ── Sequences for visual codes ──────────────────────────────
CREATE SEQUENCE IF NOT EXISTS user_visual_code_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS org_visual_code_seq START WITH 1001 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS pet_visual_code_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS rescue_case_visual_code_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS adoption_visual_code_seq START WITH 1 INCREMENT BY 1;

-- ── Backfill existing rows (deterministic by created_at) ────
WITH ranked AS (
    SELECT user_id, ROW_NUMBER() OVER (ORDER BY created_at, user_id) AS rn
    FROM users
    WHERE user_code IS NULL
)
UPDATE users u
SET user_code = 'U-' || LPAD(r.rn::TEXT, 4, '0')
FROM ranked r
WHERE u.user_id = r.user_id;

WITH ranked AS (
    SELECT organization_id, ROW_NUMBER() OVER (ORDER BY created_at, organization_id) + 1000 AS rn
    FROM organizations
    WHERE organization_code IS NULL
)
UPDATE organizations o
SET organization_code = 'O-' || r.rn::TEXT
FROM ranked r
WHERE o.organization_id = r.organization_id;

WITH ranked AS (
    SELECT pet_id, ROW_NUMBER() OVER (ORDER BY created_at, pet_id) AS rn
    FROM pets
    WHERE pet_code IS NULL
)
UPDATE pets p
SET pet_code = 'P-' || LPAD(r.rn::TEXT, 4, '0')
FROM ranked r
WHERE p.pet_id = r.pet_id;

WITH ranked AS (
    SELECT case_id, ROW_NUMBER() OVER (ORDER BY created_at, case_id) AS rn
    FROM rescue_cases
    WHERE case_code IS NULL
)
UPDATE rescue_cases rc
SET case_code = 'R-' || LPAD(r.rn::TEXT, 4, '0')
FROM ranked r
WHERE rc.case_id = r.case_id;

WITH ranked AS (
    SELECT application_id, ROW_NUMBER() OVER (ORDER BY created_at, application_id) AS rn
    FROM adoption_applications
    WHERE adoption_code IS NULL
)
UPDATE adoption_applications a
SET adoption_code = 'A-' || LPAD(r.rn::TEXT, 4, '0')
FROM ranked r
WHERE a.application_id = r.application_id;

-- ── Keep sequences in sync after backfill ───────────────────
SELECT setval(
    'user_visual_code_seq',
    GREATEST((SELECT COALESCE(MAX(SUBSTRING(user_code FROM 3)::INTEGER), 0) + 1 FROM users), 1),
    false
);

SELECT setval(
    'org_visual_code_seq',
    GREATEST((SELECT COALESCE(MAX(SUBSTRING(organization_code FROM 3)::INTEGER), 0) + 1 FROM organizations), 1001),
    false
);

SELECT setval(
    'pet_visual_code_seq',
    GREATEST((SELECT COALESCE(MAX(SUBSTRING(pet_code FROM 3)::INTEGER), 0) + 1 FROM pets), 1),
    false
);

SELECT setval(
    'rescue_case_visual_code_seq',
    GREATEST((SELECT COALESCE(MAX(SUBSTRING(case_code FROM 3)::INTEGER), 0) + 1 FROM rescue_cases), 1),
    false
);

SELECT setval(
    'adoption_visual_code_seq',
    GREATEST((SELECT COALESCE(MAX(SUBSTRING(adoption_code FROM 3)::INTEGER), 0) + 1 FROM adoption_applications), 1),
    false
);

-- ── Constraints & indexes ───────────────────────────────────
ALTER TABLE users
    ALTER COLUMN user_code SET NOT NULL;
ALTER TABLE organizations
    ALTER COLUMN organization_code SET NOT NULL;
ALTER TABLE pets
    ALTER COLUMN pet_code SET NOT NULL;
ALTER TABLE rescue_cases
    ALTER COLUMN case_code SET NOT NULL;
ALTER TABLE adoption_applications
    ALTER COLUMN adoption_code SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_users_user_code ON users(user_code);
CREATE UNIQUE INDEX IF NOT EXISTS uk_organizations_code ON organizations(organization_code);
CREATE UNIQUE INDEX IF NOT EXISTS uk_pets_pet_code ON pets(pet_code);
CREATE UNIQUE INDEX IF NOT EXISTS uk_rescue_cases_case_code ON rescue_cases(case_code);
CREATE UNIQUE INDEX IF NOT EXISTS uk_adoptions_adoption_code ON adoption_applications(adoption_code);
