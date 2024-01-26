package ru.bessik.price.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bessik.price.controller.dto.*;
import ru.bessik.price.feign.TelegramBotFeignClient;
import ru.bessik.price.feign.dto.SendMessageRequest;
import ru.bessik.price.service.ProductService;
import ru.bessik.price.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final UserService userService;
    private final ProductService productService;
    private final TelegramBotFeignClient botFeignClient;

    //todo методы update, updateAll, getPrice, getPrices перенести в новый контроллер PriceController "/api/v1/price" (Серега)

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

    // todo метод для обновления всех цен только у одного пользователя: принимает telegramId (/update-on-user/{telegramId}) (Дамир) + телеграм бот сервис

    @PostMapping("/update-all/{telegramId}")
    public StatusResponse updateAllForUser(@PathVariable String telegramId) {
        log.info("[API] update all prices for user {}",telegramId);
        productService.updateAllForUser(telegramId);
        return new StatusResponse("Цены успешно обновлены");

    }

    // todo метод getPrice для получения текущей цены (Серега)

    /**
     * Получить список цен за период по товару.
     *
     * @param request ссылка на товар и период
     * @return список цен за период
     */
    @PostMapping("/prices")
    public PriceResponse getPrices(@RequestBody PriceRequest request) {
        log.info("[API] get prices for {}", request.getProductUrl());
        return productService.getPrices(request.getProductUrl(), request.getPeriodInDays());
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

    /**
     * Отписаться от товара
     *
     * @param request данные для отписки
     * @return статус
     */
    @PostMapping("/unsubscribe")
    public StatusResponse unsubscribe(@RequestBody SubscribeRequest request) {
        log.info("[API] start unsubscribe {}", request);
        return userService.unsubscribe(request);
    }

//    @PostMapping("/unsubscribe-all")
//    public StatusResponse unsubscribeAll(@RequestBody UnsubscribeAllRequest request) {
//        log.info("[API] start unsubscribe all{}", request);
//        return userService.unsubscribeAll2(request.getTelegramId());
//    }
//
//    @GetMapping("/unsubscribe-all2/{telegramId}")
//    public StatusResponse unsubscribeAll2(@PathVariable String telegramId) {
//        log.info("[API] start unsubscribe all{}", telegramId);
//        return userService.unsubscribeAll2(telegramId);
//    }

    //delete mapping + requestParam
    @DeleteMapping(value = "/unsubscribe-all")
    public StatusResponse unsubscribeAll(@RequestParam (name = "telegramId") String telegramId) {
        log.info("[API] start unsubscribe all{}", telegramId);
        return userService.unsubscribeAll(telegramId);
    }


    @Deprecated(since = "test send message to Andrew Bessonov")
    @GetMapping("/test")
    public void test() {
        botFeignClient.sendMessage(SendMessageRequest.builder()
                .telegramId("375183651")
                .message("test")
                .build());
    }
}
