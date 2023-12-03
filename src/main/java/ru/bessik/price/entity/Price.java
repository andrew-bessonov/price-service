package ru.bessik.price.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Builder
@Table("price")
@NoArgsConstructor
@AllArgsConstructor
public class Price {

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * Наименование товара
     */
    private String productName;

    /**
     * Цена товара
     */
    private Double price;

    /**
     * Дата
     */
    private LocalDate priceDate;

    /**
     * Ссылка на товар
     */
    private String productUrl;
}
