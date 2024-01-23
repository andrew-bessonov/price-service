package ru.bessik.price.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bessik.price.controller.dto.*;
import ru.bessik.price.feign.TelegramBotFeignClient;
import ru.bessik.price.feign.dto.SendMessageRequest;
import ru.bessik.price.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final UserService userService;
    private final TelegramBotFeignClient botFeignClient;

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

    // todo отписаться от всех товаров /unsubscribe-all

    @Deprecated(since = "test send message to Andrew Bessonov")
    @GetMapping("/test")
    public void test() {
        botFeignClient.sendMessage(SendMessageRequest.builder()
                .telegramId("375183651")
                .message("test")
                .build());
    }
}
