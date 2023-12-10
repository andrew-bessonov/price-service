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
@Table("product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /**
     * id
     */
    @Id
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
    @MappedCollection(idColumn = "product_id", keyColumn = "id")
    private List<Price> prices = new ArrayList<>();

    /**
     * Подписанные пользователи
     */
    @Builder.Default
    @MappedCollection(idColumn = "product_id", keyColumn = "users_id")
    private List<User> subscribedUsers = new ArrayList<>();
}
