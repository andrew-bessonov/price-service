package ru.bessik.price.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.best-price")
public class AppProperties {

    /**
     * Период в днях, за который высчитывается лучшая цена для уведомления пользователей
     */
    private Integer period;
}
