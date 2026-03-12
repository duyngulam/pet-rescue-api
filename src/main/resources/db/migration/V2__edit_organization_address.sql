-- V2__alter_organizations_address_structure.sql

ALTER TABLE organizations
    RENAME COLUMN address TO street_address;

ALTER TABLE organizations
    ADD COLUMN  email VARCHAR(255),
    ADD COLUMN province_code   VARCHAR(20),
    ADD COLUMN province_name   VARCHAR(100),
    ADD COLUMN ward_code       VARCHAR(20),
    ADD COLUMN ward_name       VARCHAR(100);

--  index để filter nhanh theo khu vực
CREATE INDEX IF NOT EXISTS idx_org_province_code
    ON organizations(province_code);

CREATE INDEX IF NOT EXISTS idx_org_ward_code
    ON organizations(ward_code);