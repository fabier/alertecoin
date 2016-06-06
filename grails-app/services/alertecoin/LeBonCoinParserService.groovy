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
    private static final String[] MONTHS = ["ja", "f", "mar", "av", "mai", "juin", "juil", "ao", "s", "o", "n", "d"]

    ImageService imageService
    ClassifiedService classifiedService

    def getClassifieds(String url) {
        getClassifieds(url, null)
    }

    def getClassifieds(String url, Date afterDate) {
        log.info "GET ${url}"
        def document = Jsoup.parse(new URL(url), 10000)
        return getClassifieds(document, afterDate)
    }

    def getClassifieds(Document document, Date afterDate) {

        List<Classified> classifieds = new ArrayList<>()

        def elements = document.select("section.tabsContent > ul > li > a")
        for (Element element : elements) {
            String href = element.attr("href")
            href = normalizeHrefHTTPS(href)

            Classified classified = classifiedService.getClassifiedByURL(href)

            Element dateDiv = element.select("section > aside > p.item_supp").first()
            Element imageImg = element.select("div.item_image img").first()
            Element detailDiv = element.select("section.item_infos").first()

            if (dateDiv != null && detailDiv != null) {
                if (classified.name == null) {
                    String title = detailDiv.select("h2.item_title").text().trim()
                    classified.name = title

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
                        String imageUrl = imageImg.attr("src")
                        imageUrl = normalizeHrefHTTPS(imageUrl)
                        Image image = imageService.getImageByURL(imageUrl)
                        if (image != null) {
                            classified.addToImages(image)
                        }
                    }

                    // ---------------------------
                    // Gestion du parse du prix
                    // ---------------------------
                    String priceText = detailDiv.select("h3.item_price").first()?.text()?.trim()
                    if (priceText) {
                        String priceTextCleaned = priceText.replaceAll("\u00A0", "").replaceAll("€", "").replaceAll(" ", "").trim()
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
        classifieds.retainAll {
            it != null
        }

        if (!classifieds.isEmpty()) {
            log.info "Found ${classifieds.size()} classifieds..."
            classifieds.sort { a, b ->
                a.date.compareTo(b.date)
            }
            if (afterDate) {
                // On ne garde que les annonces qui datent d'au moins la "dateAfter"
                classifieds.retainAll {
                    it.date.after(afterDate)
                }
                log.info "...Retaining ${classifieds.size()} classifieds after date : ${DateFormat.getDateTimeInstance().format(afterDate)}."
            } else {
                log.info "...Retaining all classifieds (no afterDate specified)."
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
        if (href.startsWith("//")) {
            return "${prefix}:${href}"
        } else {
            return href
        }
    }

    def getAndFillExtraInfoForClassified(Classified classified) {
        log.info "GET ${classified.url}"
        def document = Jsoup.parse(new URL(classified.url), 10000)

        // Récupérer les différentes images
        Elements imageElements = document.select("section.adview_main div.thumbnails img")
        if (imageElements.isEmpty()) {
            // Potentiellement, une seule image (pas de carousel)
            imageElements = document.select("section.adview_main > div.item_image img")
        }
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

        Elements properties = document.select("section.properties > div")
        for (Element property : properties) {
            Set<String> classes = property.classNames()
            // On ne veut pas les informations "ceci est un professionnel"
            if (!classes.contains("line_pro")) {
                if (classes.contains("properties_description")) {
                    // C'est la description du bien
                    classified.description = property.select("p.value")?.first()?.html()?.trim()
                } else {
                    // C'est un autre type de propriété
                    String keyName = property.select("span.property")?.first()?.text()?.replace(":", "")?.trim()
                    Element valueElement = property.select("span.value")?.first()
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
                        Key key = Key.findOrSaveByName(keyName)
                        ClassifiedExtra classifiedExtra = ClassifiedExtra.findOrSaveByClassifiedAndKey(classified, key)
                        classifiedExtra.value = value
                        classifiedExtra.save(failOnError: true)
                    }
                }
            }
        }

        classified.save(failOnError: true)
    }
}
