package ru.bessik.price.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StartTrackingRequest {

    /**
     * Ссылка на товар для отслеживания
     */
    private String productUrl;
}
