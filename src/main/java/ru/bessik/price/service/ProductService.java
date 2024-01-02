package ru.bessik.price.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;
import ru.bessik.price.controller.dto.PriceDto;
import ru.bessik.price.controller.dto.PriceRequest;
import ru.bessik.price.entity.Product;
import ru.bessik.price.repository.ProductRepository;
import ru.bessik.price.utils.PriceMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UpdatePriceService updatePriceService;

    @Transactional
    public List<PriceDto> getPrices(PriceRequest request) {
        LocalDate startDate = LocalDate.now().minusDays(request.getPeriodInDays());
        Product product = productRepository.findByUrl(request.getProductUrl())
                .orElseThrow();
        return product.getPrices().stream()
                .filter(it -> it.getPriceDate().isAfter(startDate))
                .map(PriceMapper::toDto)
                .toList();
    }

    @Transactional
    public void update(String url) {
        updatePriceService.update(productRepository.findByUrl(url)
                .orElseThrow());
    }

    @Transactional
    public void updateAll() {
        List<Product> products = IterableUtils.toList(productRepository.findAll()); // todo брать только те товары, на которые подписаны люди
        Random random = new Random();
        for (Product product : products) {
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
