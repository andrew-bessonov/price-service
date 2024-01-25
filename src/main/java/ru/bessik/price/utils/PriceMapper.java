package ru.bessik.price.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.bessik.price.controller.dto.PriceDto;
import ru.bessik.price.entity.Price;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PriceMapper {

    public static PriceDto toDto(Price entity) {
        return PriceDto.builder()
                .currentPrice(entity.getCurrentPrice())
                .currentDate(entity.getCurrentDate())
                .build();
    }
}
