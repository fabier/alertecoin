import alertecoin.*
import com.pfabier.alertecoin.*
import grails.util.Environment

/**
 * Created by fabier on 02/11/14.
 */
class BootStrap {

    AlertService alertService

    def init = { servletContext ->
        log.info "Entering Bootstrap"

        bootstrapAll()

        if (Environment.current == Environment.DEVELOPMENT) {
            // insert Development environment specific code here
            bootstrapDevelopment()
        } else if (Environment.current == Environment.TEST) {
            // insert Test environment specific code here
            bootstrapTest()
        } else if (Environment.current == Environment.PRODUCTION) {
            // insert Production environment specific code here
            bootstrapProduction()
        }

        log.info "Exiting Bootstrap"
    }

    def bootstrapAll() {
        // Check whether the test data already exists.
        if (Role.count() == 0) {
            log.info "Creating Roles"

            Role admin = new Role(authority: "ROLE_ADMIN")
            admin.save()
            log.info "Created Role : ROLE_ADMIN"

            Role user = new Role(authority: "ROLE_USER")
            user.save()
            log.info "Created Role : ROLE_USER"

            log.info "Roles created !"
        }

        if (User.count() == 0) {
            log.info "Creating Users"

            User admin = new User(username: "pierre.fabier@gmail.com", email: "pierre.fabier@gmail.com", password: "JTXt9v6eH2Ir0vS", enabled: true, accountExpired: false, accountLocked: false)
            admin.save()
            // L'admin a tous les droits
            Role.all.each {
                UserRole.create(admin, it, true)
            }
            // L'admin voit toutes les recherches enregistrées
            Alert.all.each {
                admin.addToAlerts(it)
            }
            admin.save()
            log.info "Created User : admin"

            log.info "Users created !"
        }
    }

    def bootstrapDevelopment() {
    }

    def bootstrapTest() {

    }

    def bootstrapProduction() {

    }

    def destroy = {
    }
}