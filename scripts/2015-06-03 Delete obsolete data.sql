CREATE TEMP TABLE tempImages AS
SELECT I.id FROM image I
LEFT JOIN classified_image CI
ON CI.image_id = I.id
WHERE CI.classified_images_id IS NULL;

DELETE FROM image USING tempImages
WHERE image.id = tempImages.id;

DROP TABLE tempImages;

CREATE TEMP TABLE tempClassified AS
SELECT C.id FROM classified C
LEFT JOIN alert_classified AC
ON AC.classified_id = C.id
WHERE AC.classified_id IS NULL;

DELETE FROM classified_image USING tempClassified
WHERE classified_image.classified_images_id = tempClassified.id;

CREATE TEMP TABLE classified_extra_to_delete
(id BIGINT);

INSERT INTO classified_extra_to_delete(id)
SELECT CCE.classified_extra_id FROM classified_classified_extra CCE
INNER JOIN tempClassified TC
ON CCE.classified_classified_extras_id = TC.id;

DELETE FROM classified_classified_extra USING tempClassified
WHERE classified_classified_extra.classified_classified_extras_id = tempClassified.id;

DELETE FROM classified_extra USING classified_extra_to_delete
WHERE classified_extra.id = classified_extra_to_delete.id;

DROP TABLE classified_extra_to_delete;

DELETE FROM classified USING tempClassified
WHERE classified.id = tempClassified.id;

DROP TABLE tempClassified;