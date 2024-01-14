package ru.bessik.price.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Product;
import ru.bessik.price.repository.ProductRepository;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PiterGsmService implements UpdatePriceService {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void update(Product product) {
        String url = product.getUrl();

        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .referrer("http://www.google.com")
                    .timeout(10000)
                    .get();
        } catch (IOException e) {
            log.error("Не получилось достать данные с страницы {}", url, e);
            return;
        }

        Double price = getPrice(document, url);
        String productName = getProductName(document, url);

        if (price == null || productName == null) { // todo productName не обязательно?
            log.warn("Не удалось получить необходимые данные с сайта (цена {}, название {}", price, productName);
            return;
        }

        if (product.getName() == null) {
            product.setName(productName);
        }

        Price priceEntity = Price.builder()
                .price(price)
                .priceDate(LocalDate.now())
                .product(product)
                .build();
        product.getPrices().add(priceEntity);

        Product updateProduct = productRepository.save(product);
        log.info("success saved {} - {}", updateProduct, price);

        notificationService.checkLastPriceIsLower(updateProduct);
    }

    private String getProductName(Document document, String url) {
        Element titleElement = document.selectFirst("h1[data-product-name]");

        if (titleElement == null) {
            log.error("Не найдено наименование товара на странице {}", url);
            return null;
        }

        return titleElement.attr("data-product-name");
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
