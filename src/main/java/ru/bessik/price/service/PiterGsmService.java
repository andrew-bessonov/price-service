package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.bessik.price.controller.dto.TrackingRequest;
import ru.bessik.price.controller.dto.TrackingResponse;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Url;
import ru.bessik.price.repository.PriceRepository;
import ru.bessik.price.repository.UrlRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PiterGsmService implements PriceService {

    private final PriceRepository priceRepository;
    private final UrlRepository urlRepository;

    @Override
    public TrackingResponse startTracking(TrackingRequest request) {
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
            return TrackingResponse.builder()
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

        Optional<Url> optUrl = urlRepository.findByProductUrl(url);
        if (optUrl.isEmpty()) {
            Url entityUrl = Url.builder()
                    .productUrl(url)
                    .isNeedUpdate(true)
                    .build();
            urlRepository.save(entityUrl);
        }
        optUrl.ifPresent(it -> {
            it.setIsNeedUpdate(true);
            urlRepository.save(it);
        });

        return TrackingResponse.builder()
                .status(String.format("saved success %s", entity))
                .build();
    }

    @Override
    public void updateAll(List<Url> urls) {
        // todo
    }

    @Override
    public TrackingResponse stopTracking(TrackingRequest request) {
        urlRepository.findByProductUrl(request.getProductUrl()).ifPresent(url -> {
            url.setIsNeedUpdate(false);
            urlRepository.save(url);
        });
        return TrackingResponse.builder()
                .status("unsubscribed success")
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
