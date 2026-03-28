-- Add location name columns to rescue_cases
ALTER TABLE rescue_cases ADD COLUMN IF NOT EXISTS province_name VARCHAR(255);
ALTER TABLE rescue_cases ADD COLUMN IF NOT EXISTS ward_name VARCHAR(255);
