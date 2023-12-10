package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bessik.price.controller.dto.SubscribeRequest;
import ru.bessik.price.controller.dto.SubscribeResponse;
import ru.bessik.price.entity.Product;
import ru.bessik.price.entity.User;
import ru.bessik.price.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProductService productService;

    /**
     * Подписаться на товар.<br>
     * Если пользователя или товара не было в БД - создаем новые
     *
     * @param request Данные для подписки
     * @return статус
     */
    public SubscribeResponse startSubscribe(SubscribeRequest request) {
        User user = findUserByTelegramId(request.getTelegramId());
        Product product = productService.getByUrl(request.getProductUrl());

        user.getSubscriptions().add(product);
        User updateUser = updateUser(user);

        return SubscribeResponse.builder()
                .status(String.format("subscribed successfully %s", updateUser))
                .build();
    }

    private User findUserByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseGet(() -> User.builder()
                        .telegramId(telegramId)
                        .build());
    }

    private User updateUser(User user) {
        if (user.getId() == null) {
            log.error("Невозможно обновить пользователя без id");
            return null;
        }
        return userRepository.save(user);
    }
}
