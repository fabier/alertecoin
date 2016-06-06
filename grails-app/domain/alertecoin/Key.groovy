package alertecoin

class Key extends BaseEntity {

    static constraints = {
        name unique: true
    }

    static mapping = {
        cache true
    }
}
