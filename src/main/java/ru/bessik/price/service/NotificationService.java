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
import ru.bessik.price.properties.AppProperties;
import ru.bessik.price.utils.Utils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final String MESSAGE_TEXT = "Изменилась цена на товар %s, \nСейчас самая низкая за последний месяц (%s)";

    private final TelegramBotFeignClient telegramBotFeignClient;
    private final AppProperties appProperties;

    /**
     * Проверить последнюю цену на самую низкую за период<br>
     * Если цена самая низкая - уведомить подписанных пользователей через telegram
     *
     * @param product товар
     */
    public void checkLastPriceIsBest(Product product) {
        List<Price> pricesFromPeriod = Utils.getPricesFromPeriod(product, appProperties.getPeriod());
        if (pricesFromPeriod.size() < 2) {
            return;
        }

        Double minValue = minValueExcludeLast(pricesFromPeriod);
        Double lastValue = pricesFromPeriod.getLast().getCurrentPrice();

        if (Double.compare(minValue, lastValue) == 0) {
            return;
        }

        if (Double.compare(lastValue, minValue) < 0) {
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
                SendMessageResponse sendMessageResponse = telegramBotFeignClient
                        .sendMessage(SendMessageRequest.builder()
                        .telegramId(telegramId)
                        .message(message)
                        .build()); // todo переделать на кафку

                if (SendMessageStatus.ERROR == sendMessageResponse.getStatus()) {
                    log.error("failed to send message to client {} in telegram", telegramId);
                }
            } catch (Exception e) {
                log.error("Не удалось отправить сообщение", e);
            }
        }
    }

    private Double minValueExcludeLast(List<Price> priceList) {
        List<Double> prices = priceList.stream()
                .map(Price::getCurrentPrice)
                .toList();

        Double minValue = Double.MAX_VALUE;
        for (int i = 0; i < prices.size() - 1; i++) {
            Double currentValue = prices.get(i);
            if (currentValue < minValue) {
                minValue = currentValue;
            }
        }
        return minValue;
    }
}
