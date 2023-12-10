package ru.bessik.price.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeRequest {

    /**
     * telegram id
     */
    private String telegramId;

    /**
     * Ссылка на товар
     */
    private String productUrl;
}
