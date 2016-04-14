ALTER TABLE classified_classified_extra RENAME classified_classified_extras_id TO classified_id;

ALTER TABLE alert ALTER COLUMN description TYPE text;

UPDATE alert
SET url= replace(url, 'http://', 'https://')
WHERE url like '%http://%';