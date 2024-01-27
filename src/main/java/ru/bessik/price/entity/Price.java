package ru.bessik.price.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "product")
@Table(name = "price")
public class Price {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Цена товара
     */
    private Double currentPrice;

    /**
     * Дата
     */
    private LocalDate currentDate;

    /**
     * Товар
     */
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Override
    public String toString() {
        return String.format("%sр. от %s", getCurrentPrice(), getCurrentDate());
    }
}
