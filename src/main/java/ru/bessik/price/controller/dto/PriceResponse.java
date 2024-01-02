package ru.bessik.price.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceResponse {

    /**
     * Список цен.
     */
    private List<PriceDto> priceList;
}
