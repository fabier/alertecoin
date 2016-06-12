package alertecoin

import org.springframework.security.access.annotation.Secured

@Secured("hasRole('ROLE_ADMIN')")
class AdminController {

    def index() {
        render view: "index"
    }

    def users() {
        def users = User.all.sort { a, b ->
            a.username.compareToIgnoreCase(b.username)
        }
        render view: "users", model: [users: users]
    }

    def userAlerts(long id) {
        User user = User.get(id)
        render view: "alerts", model: [user: user, alerts: user.alerts]
    }

    def allAlerts() {
        def alerts = Alert.listOrderByUser()
        render view: "allAlerts", model: [alerts: alerts]
    }
}
