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

    def bootstrapAll(){
        // Check whether the test data already exists.
        if (Role.count() == 0) {
            log.info "Creating Roles"

            Role admin = new Role(authority: "ADMIN")
            admin.save()
            log.info "Created Role : ADMIN"

            Role user = new Role(authority: "USER")
            user.save()
            log.info "Created Role : USER"

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
            log.info "Created User : ADMIN"

            log.info "Users created !"
        }
    }

    def bootstrapDevelopment() {
        // On supprime toutes les alertes en Dev, pour ne pas envoyer des mails à tout le monde
        // On ne garde que les alertes pour pierre.fabier@gmail.com
        Alert.all.each {

            Alert alert = it
            if (alert.user.email.equals("pierre.fabier@gmail.com")) {
                // On ne fait rien, on garde l'alerte
            } else {
                // On supprime l'alerte et toutes les annonces associées
                it.classifieds.each {
                    alert.removeFromClassifieds(it)
                    it.delete()
                }
                it.delete()
            }
        }
    }

    def bootstrapTest() {

    }

    def bootstrapProduction() {

    }

    def destroy = {
    }
}