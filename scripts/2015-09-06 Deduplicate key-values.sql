CREATE TEMP TABLE temp_duplicates
(classified_id BIGINT, key_id BIGINT, counter INTEGER, classified_extra_id BIGINT);

INSERT INTO temp_duplicates
(classified_id, key_id, counter, classified_extra_id)
SELECT C.id, CE.key_id, COUNT(CE."value"), max(CE.id)
FROM classified C
INNER JOIN classified_classified_extra CCE
ON C.id = CCE.classified_classified_extras_id
INNER JOIN classified_extra CE
ON CE.id = CCE.classified_extra_id
GROUP BY C.id, CE.key_id
HAVING COUNT(CE."value") > 1;

SELECT * FROM temp_duplicates;

DELETE FROM classified_classified_extra
USING temp_duplicates TD
WHERE classified_classified_extra.classified_extra_id = TD.classified_extra_id;

DELETE FROM classified_extra
USING temp_duplicates TD
WHERE classified_extra.id = TD.classified_extra_id;

DROP TABLE temp_duplicates;