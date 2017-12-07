package alertecoin

import grails.transaction.Transactional
import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient

import java.util.regex.Matcher
import java.util.regex.Pattern

@Transactional
class ClassifiedService {

    def getClassifiedByURL(String url) {
        Long externalId = getExternalIdFromHref(url)
        Classified classified = Classified.findOrSaveByExternalId(externalId)
        if (classified.url == null) {
            classified.url = url
            classified.save(failOnError: true)
        }
        return classified
    }

    def getExternalIdFromHref(String href) {
        Long externalId = null

        Pattern pattern = Pattern.compile(".*/((\\d*)).htm.*", Pattern.CASE_INSENSITIVE)
        Matcher matcher = pattern.matcher(href)
        if (matcher.matches()) {
            String externalIdAsString = matcher.group(1)
            try {
                externalId = Long.parseLong(externalIdAsString)
            } catch (NumberFormatException nfe) {
                log.error "Erreur lors du parse de l'identifiant externe : ${externalIdAsString}"
            }
        }

        return externalId
    }

    boolean isClassifiedStillOnline(Classified classified) {
        HttpClient client = new DefaultHttpClient();
        HttpGet response = new HttpGet(classified.url);
        HttpResponse httpResp = client.execute(response);
        int code = httpResp.getStatusLine().getStatusCode();
        return code == HttpStatus.SC_OK
    }
}
