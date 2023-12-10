package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import ru.bessik.price.entity.Product;
import ru.bessik.price.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Получить список всех товаров
     *
     * @return список товаров
     */
    public List<Product> getAll() {
        return IterableUtils.toList(productRepository.findAll());
    }

    /**
     * Получить продукт по ссылке.<br>
     * Если продукта в БД не было - создать
     *
     * @param url ссылка на продукт
     * @return Продукт
     */
    public Product getByUrl(String url) {
        return productRepository.findByUrl(url).orElseGet(() -> {
            Product product = Product.builder()
                    .url(url)
                    .prices(new ArrayList<>())
                    .build();
            return productRepository.save(product); // todo мб не надо сохранять
        });
    }

    public Product updateProduct(Product product) {
        if (product.getId() == null) {
            log.error("Невозможно обновить товар с пустым id");
            return null;
        }
        return productRepository.save(product);
    }
}
