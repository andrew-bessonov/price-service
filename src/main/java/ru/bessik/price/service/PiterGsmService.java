package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.bessik.price.repository.ProductRepository;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PiterGsmService implements UpdatePriceService {

    private final ProductRepository productRepository;

    @Override
    public String getSiteUrl() {
        return "pitergsm.ru";
    }

    public Optional<String> getProductName(Document document, String url) {
        Element titleElement = document.selectFirst("h1[data-product-name]");

        if (titleElement == null) {
            log.error("Не найдено наименование товара на странице {}", url);
            return Optional.empty();
        }

        return Optional.of(titleElement.text());
    }

    public Optional<Double> getPrice(Document document, String url) {
        String priceString = "";

        try {
            Element priceElement = document.selectFirst("span.main-detail-price");
            priceString = StringUtils.deleteAny(priceElement.text(), " ");
        } catch (Exception e) {
            log.error("Не найдена цена на странице {}", url);
            return Optional.empty();
        }

        try {
            return Optional.of(Double.parseDouble(priceString));
        } catch (NumberFormatException e) {
            log.error("Не удалось преобразовать цену {}", priceString, e);
            return Optional.empty();
        }
    }
}
