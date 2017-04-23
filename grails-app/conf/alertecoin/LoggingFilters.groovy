package alertecoin

import log.AccessLog

class LoggingFilters {

    NetworkService networkService

    def filters = {
        allButImage(controller: 'image', invert: true) {
            before = {
                def remoteAddr = request.getRemoteAddr()
                boolean shouldLogToDatabase = true
                boolean shouldLogToFile = true

                if (networkService.isRequestFromLocalhost(request)) {
                    // On n'enregistre pas en base
                    shouldLogToDatabase = false
                    if (controllerName in ["common", "scan"]) {
                        // Acces depuis nagios certainement pour vérifer le bon fonctionnement du service
                        // On ne loggue pas sur fichier
                        shouldLogToFile = false
                    }
                }

                if (shouldLogToFile) {
                    log.info "${request.forwardURI} -> $params (${remoteAddr})"
                }
                if (shouldLogToDatabase) {
                    new AccessLog(
                            ip: remoteAddr,
                            path: request.forwardURI,
                            controller: controllerName,
                            action: actionName,
                            queryString: request.queryString
                    ).save()
                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
