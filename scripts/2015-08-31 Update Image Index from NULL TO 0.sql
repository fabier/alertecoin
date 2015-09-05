UPDATE classified_image
SET images_idx = 0
WHERE images_idx IS NULL;

ALTER TABLE classified_extra
ALTER COLUMN "value" TYPE TEXT;