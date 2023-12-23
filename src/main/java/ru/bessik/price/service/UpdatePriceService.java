package ru.bessik.price.service;

import ru.bessik.price.entity.Product;

public interface UpdatePriceService {

    /**
     * Обновить цену у всех товаров.
     */
    void updateAll() throws InterruptedException;

    /**
     * Обновить цену у товара.
     *
     * @param url ссылка на товар
     */
    void update(String url);

    /**
     * Обновить цену у товара.
     *
     * @param product товар
     */
    void update(Product product);
}
