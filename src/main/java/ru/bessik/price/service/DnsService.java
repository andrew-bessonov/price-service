package ru.bessik.price.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Product;
import ru.bessik.price.repository.ProductRepository;
import ru.bessik.price.utils.Utils;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DnsService implements UpdatePriceService {

    private final ProductRepository productRepository;

    @Override
    public String getSiteUrl() {
        return "dns-shop.ru";
    } // todo вырезать www //dns-shop.ru

    @Override
    @Transactional
    public void update(Product product) {
        String url = product.getUrl();
        Document document = Utils.getDocumentFromUrl(url);
        if (document == null) {
            return;
        }

        Double price = getPrice(document, url);
        if (price == null) {
            return;
        }

        String productName = getProductName(document, url);
        if (product.getName() == null) {
            product.setName(productName);
        }

        Price priceEntity = Price.builder()
                .currentPrice(price)
                .currentDate(LocalDate.now())
                .product(product)
                .build();
        product.getPrices().add(priceEntity);

        Product updateProduct = productRepository.save(product);
        log.info("new price success saved {} - {}", updateProduct, price);
    }

    private String getProductName(Document document, String url) {
        Element titleElement = document.selectFirst("h1.product-card-top__title");

        //<h1 class="product-card-top__title" data-product-title="">Клавиатура+мышь проводная Logitech Desktop MK120 черный</h1>
        //<div class="product-card-top__name">Экшн-камера Aceline DualScreen 4K черный + доп. аккумулятор</div>

        if (titleElement == null) {
            log.error("Не найдено наименование товара на странице {}", url);
            return null;
        }

        return titleElement.attr("data-product-name");
    }

    private Double getPrice(Document document, String url) {
        Element priceElement = document.selectFirst("div.product-buy__price");

        if (priceElement == null) {
            log.error("Не найдена цена на странице {}", url);
            return null; // todo заменить на кастомное исключение?
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
