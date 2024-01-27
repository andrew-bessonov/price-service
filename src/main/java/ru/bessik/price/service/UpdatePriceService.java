package ru.bessik.price.service;

import ru.bessik.price.entity.Product;

public interface UpdatePriceService {

    /**
     * Основной адрес сайта в формате "site.com"
     *
     * @return адрес сайта
     */
    String getSiteUrl();

    /**
     * Обновить цену у товара.
     *
     * @param product товар
     */
    void update(Product product);
}
