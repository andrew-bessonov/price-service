package ru.bessik.price.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bessik.price.controller.dto.StatusResponse;
import ru.bessik.price.controller.dto.SubscribeRequest;
import ru.bessik.price.entity.Product;
import ru.bessik.price.entity.User;
import ru.bessik.price.repository.ProductRepository;
import ru.bessik.price.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * Подписаться на товар.<br>
     * Если пользователя или товара не было в БД - создаем новые
     *
     * @param request Данные для подписки
     * @return статус
     */
    @Transactional
    public StatusResponse subscribe(SubscribeRequest request) {
        User user = findOrCreateUser(request.getTelegramId());
        Product product = findOrCreateProduct(request.getProductUrl());

        user.getSubscriptions().add(product);
        User savedUser = userRepository.save(user);

        return StatusResponse.builder()
                .status(String.format("subscribed successfully %s", savedUser))
                .build();
    }

    /**
     * Отписаться от товара.<br>
     * Если пользователя или товара не было в БД - логируем
     *
     * @param request Данные для отписки
     * @return статус
     */
    @Transactional
    public StatusResponse unSubscribe(SubscribeRequest request) {

        if(!userRepository.findByTelegramId(request.getTelegramId()).isPresent()) {
            return StatusResponse.builder()
                    .status(String.format("unsubscribe not successfully, " +
                            "user %s does not exists", request.getTelegramId()))
                    .build();
        }

        User user = userRepository.findByTelegramId(request.getTelegramId()).get();

        if(!user.getSubscriptions().contains(request.getProductUrl())) {
            return StatusResponse.builder()
                    .status(String.format("unsubscribe not successfully, " +
                            "subscribe %s does not exists for user %s", request.getProductUrl(), user))
                    .build();
        }

        String product = request.getProductUrl();

        user.getSubscriptions().remove(product);
        User savedUser = userRepository.save(user);

        return StatusResponse.builder()
                .status(String.format("unsubscribed successfully %s", savedUser))
                .build();
    }

    private Product findOrCreateProduct(String productUrl) {
        return productRepository.findByUrl(productUrl)
                .orElseGet(() -> Product.builder()
                        .url(productUrl)
                        .build());
    }

    private User findOrCreateUser(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseGet(() -> User.builder()
                        .telegramId(telegramId)
                        .build());
    }
}
