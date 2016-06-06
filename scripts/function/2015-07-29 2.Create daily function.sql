/**
 * Procédure lancée toutes les nuits sur le serveur pour maintenir les données de la base à jour.
 **/

CREATE OR REPLACE FUNCTION alertecoin_daily_maintenance()
RETURNS void AS
$BODY$
BEGIN

PERFORM alertecoin_delete_obsolete_data();

END
$BODY$
LANGUAGE 'plpgsql';