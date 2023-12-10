package ru.bessik.price.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bessik.price.controller.dto.SubscribeRequest;
import ru.bessik.price.controller.dto.SubscribeResponse;
import ru.bessik.price.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class SubscribeController {

    private final UserService userService;

    /**
     * Подписаться на товар
     *
     * @param request данные для подписки
     * @return статус
     */
    @PostMapping("/subscribe")
    public SubscribeResponse subscribe(@RequestBody SubscribeRequest request) {
        log.info("[API] start subscribe {}", request);
        return userService.startSubscribe(request);
    }

    /**
     * Подписаться на товар
     *
     * @param request данные для подписки
     * @return статус
     */
    @PostMapping("/unsubscribe")
    public SubscribeResponse unsubscribe(@RequestBody SubscribeRequest request) {
        log.info("[API] unsubscribe {}", request);
        return null; // todo userService.unsubscribe(request);
    }
}
