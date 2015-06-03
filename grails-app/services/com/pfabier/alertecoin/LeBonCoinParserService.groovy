package com.pfabier.alertecoin

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

class LeBonCoinParserService {

    private static final String PROXY_HTTP_INDICATOR = "http%3A%2F%2F";
    private static final String[] months = ["j", "f", "mar", "av", "mai", "juin", "juil", "ao", "s", "o", "n", "d"]

    def getClassifieds(Document document) {
        return getClassifieds(document, null)
    }

    def getClassifieds(Document document, Date afterDate) {

        List<Classified> classifieds = new ArrayList<>()

        def elements = document.select("div.list-lbc a")
        for (Element element : elements.iterator()) {
            Classified classified = new Classified()

            String href = element.attr("href")
            href = checkDecodeURL(href)
            classified.url = href

            classified.externalId = getExternalIdFromHref(href)

            String title = element.attr("title")
            classified.name = title

            Element dateDiv = element.select("div.date").first()
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

                // Jour du mois
                int dayOfMonth = Integer.parseInt(dateDayTextArray[0])
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Mois
                String monthText = dateDayTextArray[1].toLowerCase()
                int currentMonth = calendar.get(Calendar.MONTH)
                int classifiedMonth = 0
                for (int month = 0; month < months.length; month++) {
                    String monthStart = months[month]
                    if (monthText.startsWith(monthStart)) {
                        classifiedMonth = month
                        break
                    }
                }
                int deltaMonth = (currentMonth - classifiedMonth + 12) % 12
                calendar.add(Calendar.MONTH, -deltaMonth)
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

            Element imageImg = element.select("div.image img").first()
            if (imageImg) {
                String imageUrl = imageImg.attr("src")
                Image image = new Image()
                image.url = imageUrl
                image.data = getBytes(imageUrl)
                image.save()
                classified.addToImages(image)
            }

            Element detailDiv = element.select("div.detail").first()
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

            classifieds.add(classified)
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

    def checkDecodeURL(String url) {
        def indexOfHttpSlashSlash = url.indexOf(PROXY_HTTP_INDICATOR)
        if (indexOfHttpSlashSlash >= 0) {
            // Url encodée derrière un proxy
            url = url.substring(indexOfHttpSlashSlash)
            def cutAtIndex = url.indexOf("&")
            if (cutAtIndex >= 0) {
                url = url.substring(0, cutAtIndex)
            }
            log.info "final url : " + url
            return url
        } else {
            return url
        }
    }

    def getBytes(String url) {
        return new URL(url).getBytes()
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
}
