package ru.bessik.price.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Product;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static final String LINK_PATTERN_REGEX = "(http|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])";

    public static List<Price> getPricesFromPeriod(Product product, Integer periodInDays) {
        LocalDate startDate = LocalDate.now().minusDays(periodInDays);
        return product.getPrices().stream()
                .filter(it -> it.getPriceDate().isAfter(startDate))
                .toList();
    }

    public static String getDomainSiteFromUrl(String url) {
        Pattern pattern = Pattern.compile(LINK_PATTERN_REGEX);
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) {
            return matcher.group(2);
        }
        throw new RuntimeException("url is not valid");
    }

    public static Document getDocumentFromUrl(String url) { // Переделать на optional
        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .referrer("http://www.google.com")
                    .timeout(10000)
                    .get();
        } catch (IOException e) {
            log.error("Не получилось достать данные с страницы {}", url, e);
            return null;
        }
        return document;
    }
}
