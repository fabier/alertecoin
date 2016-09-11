package alertecoin

import grails.plugin.mail.MailService
import org.springframework.security.access.annotation.Secured

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@Secured(['permitAll'])
class ScanController {

    MailService mailService
    AlertService alertService

    ReentrantLock reentrantLock = new ReentrantLock()

    def index() {
        def remoteAddr = request.getRemoteAddr()
        log.debug "remoteAddr : $remoteAddr"

        Enumeration e = NetworkInterface.getNetworkInterfaces();
        Set<String> localIPAddresses = new HashSet<>()
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                localIPAddresses.add(i.getHostAddress());
            }
        }

        // On verrouille l'usage : doit être appelé par la machine hébergeant le service.
        if ("127.0.0.1".equals(remoteAddr) || "0:0:0:0:0:0:0:1".equals(remoteAddr) || localIPAddresses.contains(remoteAddr)) {
            // Méthode synchronisée pour éviter que plusieurs thread entrent en concurrence
            if (reentrantLock.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    // On vérifie si au moins une recherche n'est pas en souffrance...
                    // On scanne d'abord les alertes qui n'ont jamais été lancées
                    List<Alert> alerts = Alert.findAll("from Alert as A WHERE A.lastCheckedDate IS NULL", [max: 10])

                    if (alerts == null || alerts.isEmpty()) {
                        // On scanne ensuite les alertes qui n'ont pas encore de date de "prochaine vérification"
                        alerts = Alert.findAll("from Alert as A WHERE A.nextCheckDate IS NULL", [max: 10])
                    }

                    if (alerts == null || alerts.isEmpty()) {
                        // On vérifie finalement les alertes dans leur ordre d'execution programmée par la date de prochaine execution
                        alerts = Alert.findAll("from Alert as A order by A.nextCheckDate ASC", [max: 10])
                    }

                    if (alerts != null || !alerts.isEmpty()) {
                        for (Alert alert : alerts) {
                            if (alert != null) {
                                String delayAsString = "N/A"
                                User user = User.get(alert.userId)
                                boolean hasToCheck

                                if (alert?.nextCheckDate != null) {
                                    long now = System.currentTimeMillis()
                                    long alertCheckTime = alert.nextCheckDate.getTime()
                                    if (now > alertCheckTime) {
                                        hasToCheck = true
                                        Date delayDate = new Date(now - alertCheckTime)
                                        delayAsString = delayDate.format("mm 'min' ss 's'")
                                    } else {
                                        // Pas d'alerte à vérifier
                                        hasToCheck = false
                                    }
                                } else {
                                    hasToCheck = true
                                }

                                if (hasToCheck) {
                                    log.info "Scanning : [${alert.id}] ${alert.name} (${user.displayName ?: user.email})"
                                    alertService?.fillWithNewClassifiedsAndSendEmailIfNewFound(alert)
                                }
                            }
                        }
                    }
                } finally {
                    reentrantLock.unlock()
                }
                render text: "scan : OK"
            } else {
                // Tant pis, on essaiera plus tard
                render text: "scan : Locked, retry later..."
            }
        } else {
            log.warn("Trying to access /rankingHistory/populateNext from IP : ${remoteAddr}")
            response.sendError(404)
        }
    }
}
