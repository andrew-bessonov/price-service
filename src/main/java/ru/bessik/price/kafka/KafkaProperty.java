package ru.bessik.price.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.kafka")
public class KafkaProperty {

    private String bootstrapServers;
    private String groupId;
    private Topics topics;

    @Data
    public static class Topics {

        /**
         * Сообщения для пользователей в telegram bot.
         */
        private String telegramBotSend;
    }
}
