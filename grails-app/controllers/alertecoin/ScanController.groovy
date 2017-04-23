package alertecoin

import grails.plugin.mail.MailService
import org.springframework.security.access.annotation.Secured

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@Secured(['permitAll'])
class ScanController {

    AlertService alertService
    NetworkService networkService
    UserService userService

    ReentrantLock scanLock = new ReentrantLock()

    def beforeInterceptor = {
        boolean isRequestFromLocalhost = networkService.isRequestFromLocalhost(request)

        // On verrouille l'usage : doit être appelé par la machine hébergeant le service, ou par un administrateur
        if (isRequestFromLocalhost || userService.isAdmin()) {
            return true
        } else {
            log.warn("Trying to access restricted scan method from IP : ${request.remoteAddr}")
            response.sendError(404)
            return false
        }
    }

    def index() {
        // Méthode synchronisée pour éviter que plusieurs thread entrent en concurrence
        if (scanLock.tryLock(10, TimeUnit.SECONDS)) {
            try {
                // On vérifie si au moins une recherche n'est pas en souffrance...
                // On scanne d'abord les alertes qui n'ont jamais été lancées
                List<Alert> alerts = Alert.findAllByLastCheckedDateIsNull([max: 10])

                if (alerts == null || alerts.isEmpty()) {
                    // On scanne ensuite les alertes qui n'ont pas encore de date de "prochaine vérification"
                    alerts = Alert.findAllByNextCheckDateIsNull([max: 10])

                    if (alerts == null || alerts.isEmpty()) {
                        // On vérifie finalement les alertes dans leur ordre d'execution programmée par la date de prochaine execution
                        alerts = Alert.findAllByNextCheckDateLessThanEquals(new Date(), [max: 10, sort: "nextCheckDate", order: "asc"])
                    }
                }

                if (alerts != null && !alerts.isEmpty()) {
                    for (Alert alert : alerts) {
                        User user = User.get(alert.userId)
                        log.info "Scanning : [${alert.id}] ${alert.name} (${user.displayName ?: user.email})"
                        alertService.fillWithNewClassifiedsAndSendEmailIfNewFound(alert)
                        Thread.sleep(2000 + (Math.random() * 8000)) // Attendre entre 2s et 10s
                    }
                }
            } finally {
                scanLock.unlock()
            }
            render text: "scan : OK"
        } else {
            // Tant pis, on essaiera plus tard
            render text: "scan : Locked, retry later..."
        }
    }
}
