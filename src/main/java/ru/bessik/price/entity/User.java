package ru.bessik.price.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToMany
    @JoinTable(name = "user_subscriptions",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "product_id") })
    @Builder.Default
    private List<Product> subscriptions = new ArrayList<>();
}
