package ru.bessik.price.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Product;
import ru.bessik.price.repository.ProductRepository;
import ru.bessik.price.utils.Utils;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiteMobileService implements UpdatePriceService {

    private final ProductRepository productRepository;

    @Override
    public String getSiteUrl() {
        return "lite-mobile.ru";
    }

    @Override
    @Transactional
    public void update(Product product) {
        String url = product.getUrl();
        Document document = Utils.getDocumentFromUrl(url);
        if (document == null) {
            return;
        }

        Optional<Double> priceOpt = getPrice(document,url);
        if (priceOpt.isEmpty())
            return;
        Double price = priceOpt.get();


        Optional<String> productNameOpt = getProductName(document,url);
        if (productNameOpt.isEmpty())
            return;

        String productName = productNameOpt.get();

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

    private Optional<String> getProductName(Document document, String url) {
        Element titleElement = document.selectFirst("h1");

        if (titleElement == null) {
            log.error("Не найдено наименование товара на странице {}", url);
            return Optional.empty();
        }

        return Optional.of(titleElement.text());
    }

    private Optional<Double> getPrice(Document document, String url) {
        Element priceDiv = document.selectFirst("div.detail-card__price-cur");

        String priceString = "";
        try {
            Element priceElement = priceDiv.selectFirst("span");
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
