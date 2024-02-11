package ru.bessik.price.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.bessik.price.feign.dto.SendMessageRequest;
import ru.bessik.price.feign.dto.SendMessageResponse;

@FeignClient(name = "telegram-bot-service",
        url = "#{feignProperties.telegramBotService}",
        path = "/api/v1")
public interface TelegramBotFeignClient {

    @PostMapping("/send")
    SendMessageResponse sendMessage(@RequestBody SendMessageRequest request);

}
