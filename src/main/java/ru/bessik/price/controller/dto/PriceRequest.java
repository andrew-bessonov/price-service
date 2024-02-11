package ru.bessik.price.controller.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Data
@NoArgsConstructor
public class PriceRequest {

    /**
     * Ссылка на товар.
     */
    @NotBlank(message = "url not be null")
    private String productUrl;

    /**
     * Кол-во дней для запроса цен.
     */
    @Min(value = 1, message = "period must be greater to 1")
    @Max(value = 40, message = "period must be lower to 40")
    private Integer periodInDays = 30;
}
