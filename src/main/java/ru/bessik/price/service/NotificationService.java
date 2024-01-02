package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bessik.price.entity.Product;
import ru.bessik.price.entity.User;
import ru.bessik.price.feign.TelegramBotFeignClient;
import ru.bessik.price.feign.dto.SendMessageRequest;
import ru.bessik.price.feign.dto.SendMessageResponse;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final TelegramBotFeignClient telegramBotFeignClient;

    public void notify(Product product) {
        String message = String.format("Изменилась цена на товар %s, \n Сейчас самая низкая за последний месяц (%s)",
                product.getName(), product.getPrices().getLast());

        List<String> telegramIds = product.getSubscribedUsers().stream()
                .map(User::getTelegramId)
                .toList();
        SendMessageRequest sendMessageRequest = new SendMessageRequest(telegramIds, message);
        SendMessageResponse sendMessageResponse = telegramBotFeignClient.sendMessage(sendMessageRequest);

        log.info("{}", sendMessageResponse);
    }
}
