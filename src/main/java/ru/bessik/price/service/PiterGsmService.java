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
import ru.bessik.price.repository.PriceRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PiterGsmService implements UpdatePriceService {

    private final ProductService productService;
    private final PriceRepository priceRepository;

    @Override
    @Transactional
    public void updateAll() {
        List<Product> products = productService.getAll(); // todo брать только те товары, на которые подписаны люди
        products.forEach(this::update);
    }

    @Override
    @Transactional
    public void update(String url) {
        update(productService.getByUrl(url));
    }

    @Override
    @Transactional
    public void update(Product product) {
        String url = product.getUrl();

        Document document;
        try {
            document = Jsoup.connect(url).get();
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

        Price savedPrice = priceRepository.save(priceEntity);
        Product updateProduct = productService.updateProduct(product);
        log.info("success saved {} && {}", updateProduct, savedPrice);
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
