package alertecoin

import grails.transaction.Transactional
import java.util.zip.GZIPInputStream


class DownloadService {

	private static final DEFAULT_TIMEOUT = 10000
	private static final String FIREFOX_USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) " +
	"Gecko/20100101 Firefox/40.1"
	private static final String CHROME_USERAGENT = "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.62 Safari/537.36"

	private static final String ACCEPT_ENCODING_COMPRESSED = "gzip, deflate, br"

	byte[] download(String url, int timeout = DEFAULT_TIMEOUT) {
		log.info "GET $url"
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
		def connection = new URL(url).openConnection()
		connection.setReadTimeout(timeout)
		connection.setConnectTimeout(timeout)
		byteArrayOutputStream << connection.getInputStream()
		byteArrayOutputStream.toByteArray()
	}

	String getUrlContentAsStringFirefoxUserAgent(String sUrl, Map<String, String> requestHeaders = null, int timeout = DEFAULT_TIMEOUT) {
		requestHeaders = addUserAgent(requestHeaders, FIREFOX_USERAGENT)
		getUrlContentAsStringCustomUserAgent(sUrl, requestHeaders, timeout)
	}

	String getUrlContentAsStringChromeUserAgent(String sUrl, Map<String, String> requestHeaders = null, int timeout = DEFAULT_TIMEOUT) {
		requestHeaders = addUserAgent(requestHeaders, CHROME_USERAGENT)
		requestHeaders = addAcceptEncoding(requestHeaders, ACCEPT_ENCODING_COMPRESSED)
		requestHeaders = add(requestHeaders, "Accept-Language", "fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7,de;q=0.6,nl;q=0.5");
		requestHeaders = add(requestHeaders, "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
		getUrlContentAsStringCustomUserAgent(sUrl, requestHeaders, timeout)
	}

	Map<String, String> addAcceptEncoding(Map<String, String> requestHeaders, String acceptEncoding) {
		return add(requestHeaders, "Accept-Encoding", acceptEncoding)
	}

	Map<String, String> addUserAgent(Map<String, String> requestHeaders, String userAgent) {
		return add(requestHeaders, "User-Agent", userAgent)
	}

	Map<String, String> add(Map<String, String> requestHeaders, String key, String userAgent) {
		if (requestHeaders == null) {
			requestHeaders = [(key): userAgent]
		} else {
			requestHeaders.put(key, userAgent)
		}
		return requestHeaders
	}


	String getUrlContentAsStringCustomUserAgent(String sUrl, Map<String, String> requestHeaders = null, int timeout = DEFAULT_TIMEOUT) {
		log.info "GET $sUrl"
		URL url = new URL(sUrl)
		def connection = url.openConnection()
		connection.setReadTimeout(timeout)
		connection.setConnectTimeout(timeout)

		requestHeaders.each { k, v ->
			connection.addRequestProperty(k, v)
		}

		InputStream input = connection.getInputStream();
		input = new GZIPInputStream(input);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));

		String inputLine
		StringWriter stringWriter = new StringWriter()
		PrintWriter printWriter = new PrintWriter(stringWriter)
		while ((inputLine = bufferedReader.readLine()) != null) {
			printWriter.println(inputLine)
		}
		bufferedReader.close();
		printWriter.flush()

		return stringWriter.toString()
	}
}
