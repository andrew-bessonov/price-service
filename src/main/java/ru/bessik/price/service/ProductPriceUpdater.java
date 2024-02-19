package ru.bessik.price.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Product;
import ru.bessik.price.repository.ProductRepository;
import ru.bessik.price.repository.UserRepository;
import ru.bessik.price.utils.Utils;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductPriceUpdater {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UpdatePriceServiceFactory updatePriceServiceFactory;



    @Transactional
    public void update(Product product, UpdatePriceService service) {
        String url = product.getUrl();
        Document document = Utils.getDocumentFromUrl(url);
        if (document == null) {
            return;
        }

        Optional<Double> priceOpt = service.getPrice(document, url);
        if (priceOpt.isEmpty()) return;
        Double price = priceOpt.get();


        Optional<String> productNameOpt = service.getProductName(document, url);
        if (productNameOpt.isEmpty()) return;

        String productName = productNameOpt.get();

        if (product.getName() == null) {
            product.setName(productName);
        }

        Price priceEntity = Price.builder().currentPrice(price).currentDate(LocalDate.now()).product(product).build();
        product.getPrices().add(priceEntity);

        Product updateProduct = productRepository.save(product);
        log.info("new price success saved {} - {}", updateProduct, price);
    }
}

