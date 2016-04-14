package alertecoin

class User {

    transient springSecurityService

    String username
    String displayName
    String email
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Date dateCreated

    Date lastUpdated

    static hasMany = [alerts: Alert]

    static transients = ['springSecurityService']

    static constraints = {
        username blank: false, unique: true
        email blank: false, unique: true
        password blank: false
        displayName nullable: true
        lastUpdated nullable: true
        dateCreated nullable: true
    }

    static mapping = {
        password column: '`password`'
        table '`user`'
    }

    static mappedBy = [
            alerts: "user"
    ]

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role }
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }
}