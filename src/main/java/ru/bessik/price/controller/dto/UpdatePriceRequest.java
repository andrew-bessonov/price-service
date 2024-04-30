package ru.bessik.price.controller.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String productUrl;
}
