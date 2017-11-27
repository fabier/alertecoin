package alertecoin

class Alert extends BaseEntity {

    /**
     * URL correspondant à la recherche
     */
    String url

    /**
     * Date de dernière vérification de la liste des annonces associées a cette recherche
     */
    Date lastCheckedDate

    /**
     * Date de prochaine vérification
     */
    Date nextCheckDate

    /**
     * Intervalle entre deux vérifications
     */
    Integer checkIntervalInMinutes

    /**
     * Heure de la vérification
     */
    Integer hourOfDay

    /**
     * Date de l'annonce la plus récente associée à cette recherche
     */
    Date mostRecentClassifiedDate

    /**
     * Etat de la recherche
     */
    State state

    /**
     * Liste des annonces associées à cette recherche
     */
    List<Classified> classifieds

    static hasMany = [classifieds: Classified]

    static belongsTo = [user: User]

    static constraints = {
        name nullable: true
        description nullable: true
        url nullable: true
        state nullable: true
        lastCheckedDate nullable: true
        mostRecentClassifiedDate nullable: true
        nextCheckDate nullable: true
        checkIntervalInMinutes nullable: true
        hourOfDay nullable: true, min: 0, max: 23
    }

    static mapping = {
        url sqlType: "text"
        state enumType: "string"
    }
}
