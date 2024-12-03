ALTER TABLE vinyl
DROP INDEX description; -- Drops the existing unique constraint on description

-- Adds a unique constraint on artist_id, title, and state
ALTER TABLE vinyl
ADD CONSTRAINT unique_artist_title_state UNIQUE (artist_id, title, state);
