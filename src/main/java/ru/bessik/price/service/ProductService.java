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

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UpdatePriceServiceFactory updatePriceServiceFactory;
    private final NotificationService notificationService;

    @Transactional
    public PriceResponse getPrices(String productUrl, Integer periodInDays) {
        Product product = productRepository.findByUrl(productUrl)
                .orElseThrow();

        List<Price> prices = Utils.getPricesFromPeriod(product, periodInDays);
        List<PriceDto> priceDtos = prices.stream()
                .map(PriceMapper::toDto)
                .toList();

        return new PriceResponse(priceDtos);
    }

    @Transactional
    public void update(String url) {
        Product product = productRepository.findByUrl(url)
                .orElseThrow();
        UpdatePriceService service = updatePriceServiceFactory.getService(url);
        service.update(product);
        notificationService.checkLastPriceIsLower(product);
    }

    @Transactional
    public void updateAll() { // todo подумать над механизмом ретрая (Андрей)
        List<Product> products = IterableUtils.toList(productRepository.findAllSubscribedProduct());
        for (Product product : products) {
            threadSleepRandomTime();
            log.info("update product info {}", product);

            try {
                UpdatePriceService service = updatePriceServiceFactory.getService(product.getUrl());
                service.update(product);
                notificationService.checkLastPriceIsLower(product);
            } catch (Exception e) {
                log.error("Error when update product {}", product, e);
            }
        }
    }

    private void threadSleepRandomTime() {
        Random random = new Random();
        try {
            Thread.sleep(random.nextLong(5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
