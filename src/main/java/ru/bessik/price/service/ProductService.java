package ru.bessik.price.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import ru.bessik.price.entity.Product;
import ru.bessik.price.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Получить список всех товаров.
     *
     * @return список товаров
     */
    public List<Product> findAll() {
        return IterableUtils.toList(productRepository.findAll());
    }

    /**
     * Получить продукт по ссылке.
     *
     * @param url ссылка на продукт
     * @return Продукт
     */
    public Optional<Product> findByUrl(String url) {
        return productRepository.findByUrl(url);
    }

    /**
     * Сохранить товар.
     *
     * @param product товар
     * @return сохраненный товар
     */
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }
}
