package alertecoin

import org.apache.commons.lang.StringUtils
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
        log.info "getClassifieds : ${url}"
        def document = Jsoup.parse(new URL(url), 10000)
        return getClassifieds(document, afterDate)
    }

    def getClassifieds(Document document, Date afterDate) {

        List<Classified> classifieds = new ArrayList<>()

        def elements = document.select("div.list-lbc > a")
        for (Element element : elements) {
            String href = element.attr("href")
            href = normalizeHref(href)

            Classified classified = classifiedService.getClassifiedByURL(href)

            Element dateDiv = element.select("div.date").first()
            Element imageImg = element.select("div.image img").first()
            Element detailDiv = element.select("div.detail").first()

            if (dateDiv != null && detailDiv != null) {
                if (classified.name == null) {
                    String title = element.attr("title")
                    classified.name = title

                    String dateDayText = dateDiv.child(0).text()
                    String dateTimeText = dateDiv.child(1).text()
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

                    if (imageImg) {
                        String imageUrl = imageImg.attr("src")
                        Image image = imageService.getImageByURL(imageUrl)
                        if (image != null) {
                            classified.addToImages(image)
                        }
                    }

                    String priceText = detailDiv.select("div.price").first()?.text()?.trim()
                    if (priceText) {
                        def priceTextCleaned = priceText.replaceAll("\u00A0", "").replaceAll("€", "").replaceAll(" ", "")
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
                    classified.save()
                } else {
                    // L'annonce a déjà été scannée, aucun intérêt de rescanner la même annonce...
                }

                // On va chercher les informations supplémentaires pour cette annonce si nécessaire
                if (classified.description == null) {
                    getAndFillExtraInfoForClassified(classified)
                    classified.save()
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

    String normalizeHref(String href) {
        if (href.startsWith("//")) {
            return "http:${href}"
        } else {
            return href
        }
    }

    def getAndFillExtraInfoForClassified(Classified classified) {
        log.info "getExtraInfo : ${classified.url}"

        def document = Jsoup.parse(new URL(classified.url), 10000)

        Elements lbcImages = document.select("div.lbcImages > meta[itemprop=image]")
        for (Element lbcImage : lbcImages) {
            String imageUrl = lbcImage.attr("content")
            Image image = imageService.getImageByURL(imageUrl)
            if (image != null && !classified.images.contains(image)) {
                classified.addToImages(image)
                classified.save()
            }
        }

        Map<String, String> itemProps = getAllItemProps(document)
        for (Map.Entry<String, String> entrySet : itemProps.entrySet()) {
            String keyName = entrySet.getKey()
            String value = entrySet.getValue()
            if (keyName != null && StringUtils.isNotBlank(value)) {
                Key key = Key.findOrSaveByName(keyName)
                classified.addToClassifiedExtras(ClassifiedExtra.findOrSaveByKeyAndValue(key, value))
                classified.save()
            }
        }

        Elements lbcParams = document.select(".lbcParams tr")
        for (Element lbcParam : lbcParams) {
            Elements children = lbcParam.children()
            if (children.size() == 2) {
                Element firstChild = children.get(0)
                Element secondChild = children.get(1)
                if (firstChild.nodeName().equals("th") && secondChild.nodeName().equals("td")) {
                    String keyName = firstChild.text()
                    String value = secondChild.text()
                    if (keyName != null && StringUtils.isNotBlank(value)) {
                        Key key = Key.findOrSaveByName(keyName)
                        classified.addToClassifiedExtras(ClassifiedExtra.findOrSaveByKeyAndValue(key, value))
                        classified.save()
                    }
                }
            }
        }


        classified.description = document.select(".content")?.first()?.html()
    }

    def static String getItemProp(Document document, String itemProp) {
        String itemPropValue = null
        Elements elements = document.select("[itemprop=" + itemProp + "]")
        if (elements.size() >= 1) {
            itemPropValue = elements.get(0).text()
        }
        return itemPropValue
    }

    def static Map<String, String> getAllItemProps(Document document) {
        Map<String, String> itempProps = new HashMap<>()
        Elements elements = document.select("[itemprop]")
        for (Element element : elements) {
            String itemPropKey = element.attr("itemprop")
            String itemPropValue = element.text()
            itempProps.put(itemPropKey, itemPropValue)
        }
        return itempProps
    }
}
