package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Product;
import ru.bessik.price.entity.User;
import ru.bessik.price.feign.TelegramBotFeignClient;
import ru.bessik.price.feign.dto.SendMessageRequest;
import ru.bessik.price.feign.dto.SendMessageResponse;
import ru.bessik.price.feign.dto.SendMessageStatus;
import ru.bessik.price.utils.Utils;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final String MESSAGE_TEXT = "Изменилась цена на товар %s, \n Сейчас самая низкая за последний месяц (%s)";

    private final TelegramBotFeignClient telegramBotFeignClient;

    public void checkLastPriceIsLower(Product product) {
        List<Price> prices = Utils.getPrices(product, 30);
        double minValue = prices.stream()
                .mapToDouble(Price::getPrice)
                .min()
                .orElseThrow();
        if (Double.compare(prices.getLast().getPrice(), minValue) <= 0) {
            notify(product);
        }
    }

    public void notify(Product product) {
        log.info("Нашли самую низкую цену на товар, оповещаем пользователей");
        String message = String.format(MESSAGE_TEXT, product.getName(), product.getPrices().getLast());

        List<String> telegramIds = product.getSubscribedUsers().stream()
                .map(User::getTelegramId)
                .toList();

        for (String telegramId : telegramIds) {
            try {
                SendMessageResponse sendMessageResponse = telegramBotFeignClient.sendMessage(SendMessageRequest.builder()
                        .telegramId(telegramId)
                        .message(message)
                        .build());
                if (SendMessageStatus.ERROR == sendMessageResponse.getStatus()) {
                    log.error("failed to send message to client {} in telegram", telegramId);
                }
            } catch (Exception e) {
                log.error("Не удалось отправить сообщение", e);
            }
        }
    }
}
