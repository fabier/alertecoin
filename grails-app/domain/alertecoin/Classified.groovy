package alertecoin

class Classified extends BaseEntity {

    /**
     * Identifiant de la petite annonce
     */
    Long externalId

    /**
     * URL pour accéder à l'annonce
     */
    String url

    /**
     * Prix de l'objet
     */
    Integer price

    /**
     * Date de dépot de l'annonce
     */
    Date date

    /**
     * Liste d'images pour cette annonce
     */
    List<Image> images

    static hasMany = [images: Image, classifiedExtras: ClassifiedExtra]

    static belongsTo = Alert

    static constraints = {
        name nullable: true
        description nullable: true
        url nullable: true
        description nullable: true
        externalId nullable: true
        price nullable: true
        date nullable: true
    }

    static mapping = {
        name sqlType: "text"
        url sqlType: "text"
        description sqlType: "text"
    }
}
