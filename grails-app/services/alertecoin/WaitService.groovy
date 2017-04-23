package alertecoin

import grails.transaction.Transactional

@Transactional
class WaitService {

    long waitAtLeast(long waitinms, long lasttime) {
        long now = System.currentTimeMillis()
        if (lasttime > 0 && lasttime <= now && now <= lasttime + waitinms) {
            long waitFor = waitinms - (now - lasttime)
            Thread.sleep(waitFor)
        }
        return System.currentTimeMillis()
    }
}
