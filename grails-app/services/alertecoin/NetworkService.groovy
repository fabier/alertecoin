package alertecoin

import grails.transaction.Transactional

@Transactional
class NetworkService {

    boolean isRequestFromLocalhost(def request) {
        def remoteAddr = request.getRemoteAddr()

        // On verrouille l'usage : doit être appelé par la machine hébergeant le service, ou par un administrateur
        if (remoteAddr in ["127.0.0.1", "0:0:0:0:0:0:0:1"]) {
            return true
        } else {
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
            return localIPAddresses.contains(remoteAddr)
        }
    }
}
