package alertecoin

class LoggingFilters {

    def filters = {
        all(controller: '*', action: '*') {
            before = {
                def remoteAddr = request.getRemoteAddr()
                if ("127.0.0.1".equals(remoteAddr) && actionName.equals("index")) {
                    // Acces depuis nagios certainement pour vérifer le bon fonctionnement du service
                    // On ne loggue pas
                } else {
                    log.info "controller:$controllerName action:$actionName -> $params (from IP: ${remoteAddr})"
                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
