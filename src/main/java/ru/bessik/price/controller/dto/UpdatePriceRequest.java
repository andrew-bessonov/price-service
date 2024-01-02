package ru.bessik.price.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePriceRequest {

    /**
     * Ссылка на товар
     */
    private String productUrl;
}
