-- V5: Rename condition column to priority for rescue cases
-- Priority levels: CRITICAL, HIGH, NORMAL, LOW

-- Rename column
ALTER TABLE rescue_cases RENAME COLUMN condition TO priority;

-- Add index on priority for filtered queries
CREATE INDEX IF NOT EXISTS idx_rescue_priority ON rescue_cases(priority);

-- Migrate existing data (condition → priority mapping)
UPDATE rescue_cases SET priority = 
  CASE priority
    WHEN 'CRITICAL' THEN 'CRITICAL'
    WHEN 'INJURED' THEN 'HIGH'
    WHEN 'SICK' THEN 'HIGH'
    WHEN 'HEALTHY' THEN 'NORMAL'
    WHEN 'UNKNOWN' THEN 'NORMAL'
    ELSE 'NORMAL'
  END
WHERE priority IS NOT NULL;

-- Set default for new records
ALTER TABLE rescue_cases ALTER COLUMN priority SET DEFAULT 'NORMAL';

-- ══════════════════════════════════════════════════════════════════════════════
-- SPATIAL INDEXING - Critical for fast map queries
-- GiST (Generalized Search Tree) index for PostGIS geometry operations
-- ══════════════════════════════════════════════════════════════════════════════

-- GiST index on rescue_cases.location for ST_Within, ST_DWithin queries
CREATE INDEX IF NOT EXISTS idx_rescue_location_gist 
    ON rescue_cases USING GIST(location);

-- GiST index on organizations.location for geo queries
CREATE INDEX IF NOT EXISTS idx_org_location_gist 
    ON organizations USING GIST(location);

-- Composite index for common filtered map queries (status + priority + spatial)
-- Note: GiST doesn't support multi-column indexes with non-spatial columns,
-- so we create separate B-tree indexes for the filter columns
CREATE INDEX IF NOT EXISTS idx_rescue_status_priority 
    ON rescue_cases(status, priority) 
    WHERE is_deleted = false;

-- Partial index for active rescue cases (most common map query)
CREATE INDEX IF NOT EXISTS idx_rescue_active_location 
    ON rescue_cases USING GIST(location) 
    WHERE is_deleted = false AND status IN ('REPORTED', 'IN_PROGRESS');

-- Analyze tables to update statistics for query planner
ANALYZE rescue_cases;
ANALYZE organizations;
