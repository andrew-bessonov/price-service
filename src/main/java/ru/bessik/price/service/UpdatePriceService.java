package ru.bessik.price.service;

import ru.bessik.price.entity.Product;

public interface UpdatePriceService {

    /**
     * Обновить цену у товара.
     *
     * @param product товар
     */
    void update(Product product);
}
