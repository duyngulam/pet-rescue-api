ALTER TABLE pets_current_owner
    ADD COLUMN IF NOT EXISTS caretaker_user_id UUID;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_pets_current_owner_caretaker_user'
    ) THEN
        ALTER TABLE pets_current_owner
            ADD CONSTRAINT fk_pets_current_owner_caretaker_user
            FOREIGN KEY (caretaker_user_id)
            REFERENCES users (user_id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_current_owner_caretaker
    ON pets_current_owner (caretaker_user_id);

