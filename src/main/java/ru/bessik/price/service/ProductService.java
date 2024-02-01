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
import ru.bessik.price.entity.User;
import ru.bessik.price.repository.ProductRepository;
import ru.bessik.price.repository.UserRepository;
import ru.bessik.price.utils.PriceMapper;
import ru.bessik.price.utils.Utils;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UpdatePriceServiceFactory updatePriceServiceFactory;
    private final NotificationService notificationService;


    @Transactional
    public PriceResponse getPrice(String productUrl) {
        Product product = productRepository.findByUrl(productUrl)
                .orElseThrow(); // todo кастомное исключение
        Price lastPrice = product.getPrices().getLast();
        PriceDto priceDto = PriceMapper.toDto(lastPrice);
        return new PriceResponse(List.of(priceDto));
    }

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
        notificationService.checkLastPriceIsBest(product);
    }

    @Transactional
    public void updateAllForUser(String telegramId) {
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(); // todo не копировать реализацию, лучше вынести в отдельный
        List<Product> userSubscriptions = user.getSubscriptions();
        for (Product product : userSubscriptions) {
            threadSleepRandomTime();
            log.info("update for user {} product info {}", telegramId, product);

            try {
                UpdatePriceService service = updatePriceServiceFactory.getService(product.getUrl());
                service.update(product);
                notificationService.checkLastPriceIsBest(product);
            } catch (Exception e) {
                log.error("Error when update for user {} product {}", telegramId, product, e);
            }
        }

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
                notificationService.checkLastPriceIsBest(product);
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
