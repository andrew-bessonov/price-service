package ru.bessik.price.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {

    /**
     * Цена товара
     */
    private Double price;

    /**
     * Дата
     */
    private LocalDate priceDate;
}
