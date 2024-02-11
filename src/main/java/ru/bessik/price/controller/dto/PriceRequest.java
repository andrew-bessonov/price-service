package ru.bessik.price.controller.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PriceRequest {

    /**
     * Ссылка на товар.
     */
    @NotBlank
    private String productUrl;

    /**
     * Кол-во дней для запроса цен.
     */
    @Min(value = 1)
    @Max(value = 40)
    private Integer periodInDays = 30;
}
