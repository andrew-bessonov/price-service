package ru.bessik.price.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeResponse {

    /**
     * Статус запроса
     */
    private String status;
}
