package alertecoin

/**
 * Created by fabier on 25/04/15.
 */
abstract class BaseEntity extends BaseDomain {

    // Nom de l'entité
    String name

    // Description de l'entité
    String description

    /**
     * Location information
     */
    String location

    // Créateur de cette entrée en base
    User creator

    static constraints = {
        name nullable: true
        description nullable: true
        creator nullable: true
        location nullable: true
    }

    static mapping = {
        description type: 'text'
    }
}
