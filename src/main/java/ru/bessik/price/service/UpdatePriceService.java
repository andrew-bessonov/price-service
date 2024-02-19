package ru.bessik.price.service;

import org.jsoup.nodes.Document;

import java.util.Optional;

public interface UpdatePriceService {

    /**
     * Основной адрес сайта в формате "site.com"
     *
     * @return адрес сайта
     */
    String getSiteUrl();

    /**
     * Получить наимнование товара.
     *
     * @param document страница сайта
     * @param url      адрес сайта
     */
    Optional<String> getProductName(Document document, String url);


    /**
     * Получить стоимость товара.
     *
     * @param document страница сайта
     * @param url      адрес сайта
     */
    Optional<Double> getPrice(Document document, String url);
}
