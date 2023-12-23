package ru.bessik.price.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bessik.price.controller.dto.*;
import ru.bessik.price.service.PriceService;
import ru.bessik.price.service.UpdatePriceService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/price")
public class PriceController {

    private final UpdatePriceService updatePriceService;
    private final PriceService priceService;

    @PostMapping("/update")
    public UpdatePriceResponse updatePrice(@RequestBody UpdatePriceRequest request) {
        log.info("[API] update price {}", request.getProductUrl());
        updatePriceService.update(request.getProductUrl());
        return new UpdatePriceResponse("Цена успешно обновлена");
    }


    @PostMapping("/update-all")
    public UpdatePriceResponse updateAll() throws InterruptedException {
        log.info("[API] update all prices");
        updatePriceService.updateAll();
        return new UpdatePriceResponse("Цены успешно обновлены");
    }

    @PostMapping
    public PriceResponse getPrices(@RequestBody PriceRequest request) {
        log.info("[API] get prices for {}", request.getProductUrl());
        List<PriceDto> prices = priceService.getPrices(request);
        return new PriceResponse(prices);
    }
}
