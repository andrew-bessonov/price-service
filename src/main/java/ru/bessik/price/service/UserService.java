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

        if (user.getSubscriptions().contains(product)) {
            log.info("subscribed already exist {}", request);
            return new StatusResponse("Вы уже подписаны на этот товар");
        }

        user.getSubscriptions().add(product);
        userRepository.save(user);

        log.info("subscribed successfully {}", request);
        return new StatusResponse("Подписка успешно оформлена, обновление цен каждый день");
    }

    /**
     * Отписаться от товара.<br>
     * Если пользователя или товара не было в БД - логируем
     *
     * @param request Данные для отписки
     * @return статус
     */
    @Transactional
    public StatusResponse unsubscribe(SubscribeRequest request) {
        Optional<User> userOptional = userRepository.findByTelegramId(request.getTelegramId());

        if (userOptional.isEmpty()) {
            log.error(String.format("unsubscribe not successfully, " +
                    "user %s does not exists", userOptional));
            return new StatusResponse("У вас нет текущих подписок");
        }

        Optional<Product> productOptional = productRepository.findByUrl(request.getProductUrl());

        if (productOptional.isEmpty()) {
            log.error(String.format("unsubscribe not successfully, " +
                    "product %s does not exists", productOptional));
            return new StatusResponse("Нет подписки на данный товар");
        }

        User user = userOptional.get();
        Product product = productOptional.get();

        if (!user.getSubscriptions().contains(product)) {
            log.error(String.format("unsubscribe not successfully, " +
                    "subscribe %s does not exists on user %s", product, user));
            return new StatusResponse("Вы не подписаны на данный товар");
        }

        user.getSubscriptions().remove(product);
        User savedUser = userRepository.save(user);

        log.info("unsubscribed successfully {}", savedUser);
        String responseMessage = product.getName() == null ?
                "Вы успешно отписались" :
                String.format("Вы успешно отписались от %s", product);

        return new StatusResponse(responseMessage);
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
