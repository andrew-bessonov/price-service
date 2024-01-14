package ru.bessik.price.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Product;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static List<Price> getPrices(Product product, Integer periodInDays) {
        LocalDate startDate = LocalDate.now().minusDays(periodInDays);
        return product.getPrices().stream()
                .filter(it -> it.getPriceDate().isAfter(startDate))
                .toList();
    }
}
