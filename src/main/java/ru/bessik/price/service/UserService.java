package ru.bessik.price.service;

import jakarta.transaction.Transactional;
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
    @Transactional
    public SubscribeResponse startSubscribe(SubscribeRequest request) {
        User user = findOrCreateUser(request.getTelegramId());
        Product product = findOrCreateProduct(request);

        user.getSubscriptions().add(product);
        User savedUser = userRepository.save(user);

        return SubscribeResponse.builder()
                .status(String.format("subscribed successfully %s", savedUser))
                .build();
    }

    private Product findOrCreateProduct(SubscribeRequest request) {
        return productService.findByUrl(request.getProductUrl())
                .orElseGet(() -> Product.builder()
                        .url(request.getProductUrl())
                        .build());
    }

    private User findOrCreateUser(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseGet(() -> User.builder()
                        .telegramId(telegramId)
                        .build());
    }
}
