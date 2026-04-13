-- ============================================================
-- V8: Lightweight Geo locations (last-known + active presence)
-- ============================================================

CREATE TABLE IF NOT EXISTS user_geo_locations (
    user_id       UUID PRIMARY KEY REFERENCES users (user_id) ON DELETE CASCADE,
    lat           DOUBLE PRECISION NOT NULL,
    lng           DOUBLE PRECISION NOT NULL,
    is_active     BOOLEAN NOT NULL DEFAULT TRUE,
    last_seen_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_user_geo_locations_active_last_seen
    ON user_geo_locations (is_active, last_seen_at DESC);

CREATE INDEX IF NOT EXISTS idx_user_geo_locations_lat_lng
    ON user_geo_locations (lat, lng);

COMMENT ON TABLE user_geo_locations IS 'Realtime last-known user locations for geo presence';
COMMENT ON COLUMN user_geo_locations.is_active IS 'False when user has not sent location updates recently';
