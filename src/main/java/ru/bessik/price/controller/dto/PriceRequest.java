package ru.bessik.price.controller.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PriceRequest {

    /**
     * Ссылка на товар.
     */
    private String productUrl;

    /**
     * Кол-во дней для запроса цен.
     */
    private Integer periodInDays;
}
