package ru.bessik.price.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.feign")
public class FeignProperties {

    /**
     * Ссылка на сервис telegram бота.
     */
    private String telegramBotService;
}
