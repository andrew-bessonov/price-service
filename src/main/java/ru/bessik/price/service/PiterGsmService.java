package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.bessik.price.controller.dto.StartTrackingRequest;
import ru.bessik.price.controller.dto.StartTrackingResponse;
import ru.bessik.price.entity.Price;
import ru.bessik.price.repository.PriceRepository;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PiterGsmService implements PriceService {

    private final PriceRepository priceRepository;

    @Override
    public StartTrackingResponse startTracking(StartTrackingRequest request) {
        LocalDate now = LocalDate.now();
        String url = request.getProductUrl();

        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            log.error("Не получилось достать данные с страницы {}", url, e);
            return null;
        }

        Double price = getPrice(document, url);
        String productName = getProductName(document, url);

        if (price == null || productName == null) {
            return StartTrackingResponse.builder()
                    .status("Не найдены нужные поля на сайте")
                    .build();
        }

        Price entity = Price.builder()
                .priceDate(now)
                .productName(productName)
                .productUrl(url)
                .price(price)
                .build();

        priceRepository.save(entity);

        return StartTrackingResponse.builder()
                .status(String.format("saved success %s", entity))
                .build();
    }

    private String getProductName(Document document, String url) {
        Element titleElement = document.selectFirst("h1[data-product-name]");

        if (titleElement != null) {
            return titleElement.attr("data-product-name");
        } else {
            log.error("Не найдено наименование товара на странице {}", url);
            return null;
        }
    }

    private Double getPrice(Document document, String url) {
        Element priceElement = document.selectFirst("span.main-detail-price");

        if (priceElement == null) {
            log.error("Не найдена цена на странице {}", url);
            return null;
        }

        String priceString = StringUtils.deleteAny(priceElement.text(), " ");
        try {
            return Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            log.error("Не удалось преобразовать цену {}", priceString, e);
            return null;
        }
    }
}
