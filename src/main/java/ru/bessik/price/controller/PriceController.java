package ru.bessik.price.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bessik.price.controller.dto.PriceRequest;
import ru.bessik.price.controller.dto.PriceResponse;
import ru.bessik.price.controller.dto.StatusResponse;
import ru.bessik.price.controller.dto.UpdatePriceRequest;
import ru.bessik.price.service.ProductService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")

public class PriceController {
    private final ProductService productService;

    /**
     * Получить текущую цену.
     *
     * @param url ссылка на товар и период
     * @return последняя цена
     */
    @GetMapping("/price")
    public PriceResponse getPrice(@NotBlank @RequestParam("url") String url) {
        log.info("[API] get price for {}", url);
        return productService.getPrice(url);
    }

    /**
     * Обновить данные о товаре.
     *
     * @param request ссылка на товар
     * @return статус
     */
    @PostMapping("/price/update")
    public StatusResponse update(@Valid @RequestBody UpdatePriceRequest request) {
        log.info("[API] update price {}", request.getProductUrl());
        productService.update(request.getProductUrl());
        return new StatusResponse("Цена успешно обновлена");
    }

    /**
     * Получить список цен за период по товару.
     *
     * @param request ссылка на товар и период
     * @return список цен за период
     */
    @PostMapping("/prices")
    public PriceResponse getPrices(@Valid @RequestBody PriceRequest request) {
        log.info("[API] get prices for {}", request.getProductUrl());
        return productService.getPrices(request.getProductUrl(), request.getPeriodInDays());
    }

    /**
     * Обновить данные о всех товарах.
     *
     * @return статус
     */
    @PostMapping("/prices/update")
    public StatusResponse updateAll() {
        log.info("[API] update all prices");
        productService.updateAll();
        return new StatusResponse("Цены успешно обновлены");
    }

    /**
     * Обновить цены всех товаров пользователя.
     *
     * @return статус
     */
    @PostMapping("/prices/update-all/{telegramId}")
    public StatusResponse updateAllForUser(@NotBlank @PathVariable String telegramId) {
        log.info("[API] update all prices for user {}", telegramId);
        productService.updateAllForUser(telegramId);
        return new StatusResponse("Цены успешно обновлены");

    }
}
