package alertecoin

class LoggingFilters {

    def filters = {
        all(controller: '*', action: '*') {
            before = {
                def remoteAddr = request.getRemoteAddr()
                log.info "controller:$controllerName action:$actionName -> $params (from ${remoteAddr})"
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
