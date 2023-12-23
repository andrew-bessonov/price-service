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
@Table(name = "product")
@EqualsAndHashCode(exclude = "subscribedUsers")
public class Product {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название товара
     */
    private String name;

    /**
     * Ссылка на товар
     */
    private String url;

    /**
     * Цена товара
     */
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Price> prices = new ArrayList<>();

    /**
     * Подписанные пользователи
     */
    @Builder.Default
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> subscribedUsers = new ArrayList<>();
}
