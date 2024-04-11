package ru.bessik.price.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramBotMessage {

    /**
     * Идентификатор пользователя в telegram.
     */
    private String telegramId;

    /**
     * Сообщение для пользователя.
     */
    private String message;
}

