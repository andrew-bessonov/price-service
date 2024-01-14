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

import java.util.Optional;

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

        Optional<User> userOptional = userRepository.findByTelegramId(request.getTelegramId());

        if(userOptional.isEmpty()) {
            log.error(String.format("unsubscribe not successfully, " +
                    "user %s does not exists", userOptional));
            return StatusResponse.builder()
                    .status("Не удалось отписаться")
                    .build();
        }

        Optional<Product> productOptional = productRepository.findByUrl(request.getProductUrl());

        if(productOptional.isEmpty()) {
            log.error(String.format("unsubscribe not successfully, " +
                    "subscribe %s does not exists", productOptional));
            return StatusResponse.builder()
                    .status("Нет подписки на данный товар")
                    .build();
        }

        User user = userOptional.get();
        Product product = productOptional.get();

        if(!user.getSubscriptions().contains(product)) {
            log.error(String.format("unsubscribe not successfully, " +
                    "subscribe %s does not exists on user %s", product, user));
            return StatusResponse.builder()
                    .status("Вы не подписаны на данный товар")
                    .build();
        }

        user.getSubscriptions().remove(product);
        User savedUser = userRepository.save(user);
        log.info(String.format("unsubscribed successfully %s", savedUser));

        return StatusResponse.builder()
                .status(String.format("Успешно отписались от %s", product))
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
