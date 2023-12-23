package ru.bessik.price.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Product;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class PiterGsmService implements UpdatePriceService {

    private final ProductService productService;
    private final PriceService priceService;

    @Override
    @Transactional
    public void updateAll() throws InterruptedException {
        List<Product> products = productService.findAll(); // todo брать только те товары, на которые подписаны люди
        for (Product product : products) {
            log.info("update product info {}", product);
            Random random = new Random();
            Thread.sleep(random.nextLong(5000));
            update(product);
        }
    }

    @Override
    @Transactional
    public void update(String url) {
        update(productService.findByUrl(url)
                .orElseThrow());
    }

    @Override
    @Transactional
    public void update(Product product) {
        String url = product.getUrl();

        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();
        } catch (IOException e) {
            log.error("Не получилось достать данные с страницы {}", url, e);
            return;
        }

        Double price = getPrice(document, url);
        String productName = getProductName(document, url);

        if (price == null || productName == null) {
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

        Product updateProduct = productService.save(product);
        log.info("success saved {}", updateProduct);
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
