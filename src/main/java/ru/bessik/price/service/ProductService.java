package ru.bessik.price.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import ru.bessik.price.controller.dto.PriceDto;
import ru.bessik.price.controller.dto.PriceResponse;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Product;
import ru.bessik.price.repository.ProductRepository;
import ru.bessik.price.utils.PriceMapper;
import ru.bessik.price.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UpdatePriceService updatePriceService;

    @Transactional
    public PriceResponse getPrices(String productUrl, Integer periodInDays) {
        Product product = productRepository.findByUrl(productUrl)
                .orElseThrow();

        List<Price> prices = Utils.getPrices(product, periodInDays);
        List<PriceDto> priceDtos = prices.stream()
                .map(PriceMapper::toDto)
                .toList();

        return new PriceResponse(priceDtos);
    }

    @Transactional
    public void update(String url) {
        updatePriceService.update(productRepository.findByUrl(url)
                .orElseThrow());
    }

    @Transactional
    public void updateAll() {
        List<Product> productList = IterableUtils.toList(productRepository.findAll());
        List<Product> productsWithSubscribers = productList.stream()
                .filter(product -> !product.getSubscribedUsers().isEmpty())
                .toList();

        Random random = new Random();
        for (Product product : productsWithSubscribers) {
            log.info("update product info {}", product);
            try {
                Thread.sleep(random.nextLong(5000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            updatePriceService.update(product);
        }
    }
}
