package ru.bessik.price.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bessik.price.controller.dto.StatusResponse;
import ru.bessik.price.controller.dto.SubscribeRequest;
import ru.bessik.price.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final UserService userService;

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

    /**
     * Отисать пользователя от всех товаров
     *
     * @return telegramId для отписки из запроса
     */
    @DeleteMapping(value = "/unsubscribe-all")
    public StatusResponse unsubscribeAll(@RequestParam(name = "telegramId") String telegramId) {
        log.info("[API] start unsubscribe all{}", telegramId);
        return userService.unsubscribeAll(telegramId);
    }
}
