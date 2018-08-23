package alertecoin

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class LeBonCoinParserService {

    private static final String PROXY_HTTP_INDICATOR = "http%3A%2F%2F"
    private static final String[] MONTHS = [
	"ja",
	"f",
	"mar",
	"av",
	"mai",
	"juin",
	"juil",
	"ao",
	"s",
	"o",
	"n",
	"d"
    ]
    private static final int TIMEOUT = 10000 // 10 sec
    private static final long TIMEOUT_ERROR_DELAY = 60 * 60 * 1000 // 1h

    ImageService imageService
    ClassifiedService classifiedService
    WaitService waitService
    DownloadService downloadService

    long lastGetTimestamp = 0l
    long nextCallDate = 0l

    List<Classified> getClassifieds(String url) {
	getClassifieds(url, null)
    }

    List<Classified> getClassifieds(String url, Date afterDate) {
	Document document = tryGetDocument(url)
	if (document != null) {
	    return getClassifieds(document, afterDate)
	} else {
	    return null
	}
    }

    Document tryGetDocument(String url, int minDelay = 500) {
	if (System.currentTimeMillis() > nextCallDate) {
	    try {
		lastGetTimestamp = waitService.waitAtLeast(minDelay, lastGetTimestamp)
		log.info "GET ${url}"
		String data = downloadService.getUrlContentAsStringChromeUserAgent(url)
		def document = Jsoup.parse(data)
		// Pas d'erreur, on met nextCallDate à 0
		nextCallDate = 0l
		return document
	    } catch (IOException e) {
		log.warn e.getMessage(), e
		nextCallDate = System.currentTimeMillis() + TIMEOUT_ERROR_DELAY
		log.info "Setting nextCallDate to : ${new Date(nextCallDate)}"
		return null
	    }
	} else {
	    log.info "Due to previous TIMEOUT error [nextCallDate:${new Date(nextCallDate)}], no HTTP GET to : ${url}"
	    return null
	}
    }

    def getClassifieds(Document document, Date afterDate) {

	List<Classified> classifieds = new ArrayList<>()

	def elements = document.select("li[data-qa-id=aditem_container] > a")
	for (Element element : elements) {
	    String href = element.attr("href")
	    href = normalizeHrefHTTPS(href)

	    Classified classified = classifiedService.getClassifiedByURL(href)

	    Element dateDiv = element.select("div[data-qa-id=listitem_date]").first()
	    Element imageImg = element.select("div.item_image span.lazyload").first()

	    if (dateDiv != null) {
		if (classified.name == null) {
		    classified.name = element.select("section > p > span")?.first()?.text()?.trim()
                    classified.location = element.select("section > div > p[itemprop=availableAtOrFrom]")?.first()?.text().trim()
                    
		    // ---------------------------
		    // Gestion du parse de la date
		    // ---------------------------

		    // ownText permet de ne pas récupérer "Urgent" contenu dans un span
		    String dateText = dateDiv.ownText().trim()
		    String[] dateTextArray = dateText.split(", ")
		    String dateDayText = dateTextArray[0].trim()
		    String dateTimeText = dateTextArray[1].trim()
		    Calendar calendar = new GregorianCalendar()
		    calendar.set(Calendar.SECOND, 0)
		    calendar.set(Calendar.MILLISECOND, 0)
		    if ("aujourd'hui".equalsIgnoreCase(dateDayText)) {
			// Pas de changement de date, c'est aujourd'hui !
		    } else if ("hier".equalsIgnoreCase(dateDayText)) {
			// On positionne à la date d'hier
			calendar.add(Calendar.DAY_OF_YEAR, -1)
		    } else {
			String[] dateDayTextArray = dateDayText.split("\\s")

			// Mois
			String monthText = dateDayTextArray[1].toLowerCase()
			int currentMonth = calendar.get(Calendar.MONTH)
			int classifiedMonth = 0
			for (int month = 0; month < MONTHS.length; month++) {
			    String monthStart = MONTHS[month]
			    if (monthText.startsWith(monthStart)) {
				classifiedMonth = month
				break
			    }
			}
			if (classifiedMonth > currentMonth) {
			    // Changement d'année, (ex : l'annonce est datée de décembre, et on est en janvier)
			    calendar.add(Calendar.YEAR, -1)
			}
			calendar.set(Calendar.MONTH, classifiedMonth)

			// Jour du mois
			int dayOfMonth = Integer.parseInt(dateDayTextArray[0])
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
		    }
		    try {
			Date date = new SimpleDateFormat("hh:mm", Locale.FRENCH).parse(dateTimeText)
			Calendar tempCalendar = Calendar.getInstance()
			tempCalendar.setTime(date)
			calendar.set(Calendar.HOUR_OF_DAY, tempCalendar.get(Calendar.HOUR_OF_DAY))
			calendar.set(Calendar.MINUTE, tempCalendar.get(Calendar.MINUTE))
			calendar.set(Calendar.SECOND, 0)
			calendar.set(Calendar.MILLISECOND, 0)
			classified.date = calendar.getTime()
		    } catch (ParseException pe) {
			// On n'a pas réussi à parser, tant pis l'horaire a un format inconnu
			log.warn "Format horaire inconnu : ${dateTimeText}"
		    }

		    // ---------------------------
		    // Gestion du parse de l'image
		    // ---------------------------
		    if (imageImg) {
			String imageUrl = imageImg.attr("data-imgsrc")
			imageUrl = normalizeHrefHTTPS(imageUrl)
			Image image = imageService.getImageByURL(imageUrl)
			if (image != null) {
			    classified.addToImages(image)
			}
		    }

		    // ---------------------------
		    // Gestion du parse du prix
		    // ---------------------------
		    String priceText = element.select("span[itemprop=price]").first()?.text()?.trim()
		    if (priceText) {
			String priceTextCleaned = priceText
			["\u00A0", "C.C.", "€", " "].each {
			    priceTextCleaned = priceTextCleaned.replaceAll(it, "")
			}
			priceTextCleaned = priceTextCleaned.trim()
			def pricesTextCleaned = priceTextCleaned.split("-")
			try {
			    if (pricesTextCleaned.length > 0) {
				classified.price = Integer.parseInt(pricesTextCleaned[0])
			    }
			} catch (NumberFormatException nfe) {
			    // le parse est incorrect
			    log.error "Erreur lors du parse du prix : ${priceText.encodeAsHTML()}"
			}
		    }

		    // On a des modifications à enregistrer
		    classified.save(failOnError: true)
		} else {
		    // L'annonce a déjà été scannée, aucun intérêt de rescanner la même annonce...
		}

		// On va chercher les informations supplémentaires pour cette annonce si nécessaire
		if (classified.description == null) {
		    getAndFillExtraInfoForClassified(classified)
		    classified.save(failOnError: true)
		}

		// On ajoute cette annonce à la liste des annonces pour cette alerte
		classifieds.add(classified)
	    } else {
		// Il ne doit pas s'agir d'une annonce, mais d'un autre type de div...
		log.warn "Impossible de récupérer des informations d'annonce pour le lien : ${href}"
	    }
	}

	// On ne garde pas les pointeurs vers null
	classifieds.retainAll { it != null }

	if (!classifieds.isEmpty()) {
	    log.debug "Found ${classifieds.size()} classifieds..."
	    classifieds.sort { a, b ->
		a.date.compareTo(b.date)
	    }
	    if (afterDate) {
		// On ne garde que les annonces qui datent d'au moins la "dateAfter"
		classifieds.retainAll {
		    it.date.after(afterDate)
		}
		log.debug "...Retaining ${classifieds.size()} classifieds after date : ${DateFormat.getDateTimeInstance().format(afterDate)}."
	    } else {
		log.debug "...Retaining all classifieds (no afterDate specified)."
	    }
	} else {
	    log.warn "Impossible to get classifieds from leboncoin !"
	}

	return classifieds
    }

    String normalizeHrefHTTPS(String href) {
	return normalizeHref(href, "https")
    }

    String normalizeHref(String href, String prefix = "http") {
	if(href.endsWith("/")){
	    href= href.substring(0,href.length()-1)
	}
	if (href.startsWith("//")) {
	    return "${prefix}:${href}"
	} else if(href.startsWith("/")){
	    return "https://www.leboncoin.fr${href}"
	}else{
	    return href
	}
    }

    def getAndFillExtraInfoForClassified(Classified classified) {
	Document document = tryGetDocument(classified.url)
	if (document != null) {
	    // Récupérer les différentes images
	    Elements imageElements = document.select("img[alt=image-galerie-0]")
	    for (Element imageElement : imageElements) {
		// Pour avoir l'image en grand, il suffit de remplacer "thumbs" par "images" dans l'URL
		String imageUrl = imageElement.attr("src").replace("thumbs", "images")
		imageUrl = normalizeHrefHTTPS(imageUrl)
		Image image = imageService.getImageByURL(imageUrl)
		if (image != null && (classified.images == null || !classified.images.contains(image))) {
		    classified.addToImages(image)
		    classified.save(failOnError: true)
		}
	    }

	    // Récupérer les différents attributs
	    // 1. Description
	    Elements descriptionElements = document.select("div[data-qa-id=adview_description_container]")
	    if (descriptionElements.size() == 1) {
		Element descriptionElement = descriptionElements.first()
		// C'est la description du bien
		classified.description = descriptionElement.select("span")?.first()?.html()?.trim()
	    }

	    // 2. Localisation
	    // Elements locationElements = document.select("div[data-qa-id=adview_location_informations]")
	    // if (locationElements.size() == 1) {
            //	Element locationElement = locationElements.first()
            //  C'est la localisation du bien
            //	classified.location = locationElement.select("span")?.first()?.text()?.trim()
	    // }

	    // 3. Autres properties
	    Elements propertyDivs = document.select("div[data-qa-id=criteria_container] > div[data-qa-id]")
	    for (Element propertyDiv : propertyDivs) {
		Elements keyAndValueDivs = propertyDiv.select("div > div[class]")

		String keyName = keyAndValueDivs?.first()?.text()?.replace(":", "")?.trim()
		Element valueElement = keyAndValueDivs?.get(1)
		String value
		if (valueElement != null) {
		    if (valueElement.children().isEmpty()) {
			value = valueElement.text().trim()
		    } else {
			Elements aElements = valueElement.select("a")
			if (aElements.size() == 1) {
			    value = aElements.first().text().trim()
			}
		    }
		}
		if (value != null) {
		    Key key = Key.findByName(keyName)
		    if (key == null) {
			key = new Key(name: keyName)
			key.save(flush: true, failOnError: true)
		    }
		    ClassifiedExtra classifiedExtra = ClassifiedExtra.findOrSaveByClassifiedAndKey(classified, key)
		    classifiedExtra.value = value
		    classifiedExtra.save(failOnError: true)
		}
	    }
	}
    }
}
