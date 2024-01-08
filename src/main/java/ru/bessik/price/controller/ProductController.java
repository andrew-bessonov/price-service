package ru.bessik.price.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bessik.price.controller.dto.*;
import ru.bessik.price.feign.TelegramBotFeignClient;
import ru.bessik.price.feign.dto.SendMessageRequest;
import ru.bessik.price.service.ProductService;
import ru.bessik.price.service.UserService;
import ru.bessik.price.utils.PriceMapper;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final TelegramBotFeignClient botFeignClient;

    /**
     * Обновить данные о товаре.
     *
     * @param request ссылка на товар
     * @return статус
     */
    @PostMapping("/update")
    public StatusResponse update(@RequestBody UpdatePriceRequest request) {
        log.info("[API] update price {}", request.getProductUrl());
        productService.update(request.getProductUrl());
        return new StatusResponse("Цена успешно обновлена");
    }

    /**
     * Обновить данные о всех товарах.
     *
     * @return статус
     */
    @PostMapping("/update-all")
    public StatusResponse updateAll() {
        log.info("[API] update all prices");
        productService.updateAll();
        return new StatusResponse("Цены успешно обновлены");
    }

    /**
     * Получить список цен за период по товару.
     *
     * @param request ссылка на товар и период
     * @return список цен за период
     */
    @PostMapping("/prices")
    public PriceResponse getPrices(@RequestBody PriceRequest request) {
        log.info("[API] get prices for {}", request.getProductUrl());
        List<PriceDto> prices = productService.getPrices(request.getProductUrl(), request.getPeriodInDays()).stream()
                .map(PriceMapper::toDto)
                .toList();
        return new PriceResponse(prices);
    }

    /**
     * Подписаться на товар
     *
     * @param request данные для подписки
     * @return статус
     */
    @PostMapping("/subscribe")
    public StatusResponse subscribe(@RequestBody SubscribeRequest request) {
        log.info("[API] start subscribe {}", request);
        return userService.subscribe(request);
    }

    // todo отписаться

    @GetMapping("/test")
    public void test() {
        botFeignClient.sendMessage(SendMessageRequest.builder()
                .telegramId("375183651")
                .message("test")
                .build());
    }
}
