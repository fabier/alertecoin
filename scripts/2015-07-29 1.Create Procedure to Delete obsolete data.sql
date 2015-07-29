/**
 * Supprime les données obsoletes de la base de données
 **/

CREATE OR REPLACE FUNCTION alertecoin_delete_obsolete_data()
RETURNS void AS
$BODY$
BEGIN

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

DELETE FROM classified USING tempClassified
WHERE classified.id = tempClassified.id;

DROP TABLE tempClassified;

END
$BODY$
LANGUAGE 'plpgsql';