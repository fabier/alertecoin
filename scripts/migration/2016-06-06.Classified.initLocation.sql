UPDATE classified C
SET location = sub."value"
FROM (
    SELECT CE.classified_id, CE."value"
    FROM "key" K
    INNER JOIN classified_extra CE
    ON CE.key_id = K.id
    WHERE K."name" = 'Ville'
) AS sub
WHERE sub.classified_id = C.id
AND C.location IS NULL;