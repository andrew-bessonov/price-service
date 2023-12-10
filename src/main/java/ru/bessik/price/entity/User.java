package ru.bessik.price.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Table("users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * telegram id
     */
    private String telegramId;

    /**
     * Имя пользователя в telegram
     */
    private String telegramUsername;

    /**
     * Номер телефона
     */
    private String phoneNumber;

    /**
     * Подписки на товары
     */
    @Builder.Default
    @MappedCollection(idColumn = "users_id", keyColumn = "product_id")
    private List<Product> subscriptions = new ArrayList<>();
}
