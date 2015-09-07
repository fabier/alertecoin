CREATE TEMP TABLE external_id_duplicated
(external_id BIGINT);

CREATE TEMP TABLE temp_classified
(id BIGINT, external_id BIGINT, joint_id BIGINT);

INSERT INTO external_id_duplicated
(external_id)
SELECT external_id from classified C
GROUP BY external_id
HAVING count(*) > 1;

INSERT INTO temp_classified
(id, external_id)
SELECT C.id, C.external_id
FROM external_id_duplicated EID
INNER JOIN classified C
ON C.external_id = EID.external_id;

UPDATE temp_classified TC1
SET joint_id = TC2.id
FROM
    (
        SELECT external_id, min(id) AS id
        FROM temp_classified
        GROUP BY external_id
    ) TC2
WHERE TC2.external_id = TC1.external_id;

DELETE FROM temp_classified
WHERE id = joint_id;

UPDATE alert_classified AC
SET classified_id = TC.joint_id
FROM temp_classified TC
WHERE TC.id = AC.classified_id;

DELETE FROM classified_classified_extra
USING temp_classified TC
WHERE classified_classified_extra.classified_classified_extras_id = TC.id;

DELETE FROM classified_image
USING temp_classified TC
WHERE classified_image.classified_images_id = TC.id;

DELETE FROM classified
USING temp_classified TC
WHERE classified.id = TC.id;

DROP TABLE external_id_duplicated;
DROP TABLE temp_classified;