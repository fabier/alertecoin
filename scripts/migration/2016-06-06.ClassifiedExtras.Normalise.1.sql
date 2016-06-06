CREATE TABLE classified_extra_new AS
SELECT C.id AS classified_id, K.id AS key_id, CE."value", CE.version, CE.date_created, CE.last_updated
FROM classified C
INNER JOIN classified_classified_extra CCE
ON C.id = CCE.classified_id
INNER JOIN classified_extra_old CE
ON CE.id = CCE.classified_extra_id
INNER JOIN "key" K
ON K.id = CE.key_id;

SELECT * FROM classified_extra_old CE
INNER JOIN classified_classified_extra CCE
ON CE.id = CCE.classified_extra_id;

-- ALTER TABLE classified_extra RENAME TO classified_extra_old;
-- ALTER TABLE classified_extra_new RENAME TO classified_extra;