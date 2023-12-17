package ru.bessik.price.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bessik.price.controller.dto.UpdatePriceRequest;
import ru.bessik.price.controller.dto.UpdatePriceResponse;
import ru.bessik.price.service.UpdatePriceService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/price")
public class PriceController {

    private final UpdatePriceService updatePriceService;

    @PostMapping("/update")
    public UpdatePriceResponse updatePrice(@RequestBody UpdatePriceRequest request) {
        log.info("[API] update price {}", request.getProductUrl());
        updatePriceService.update(request.getProductUrl());
        return new UpdatePriceResponse("Цена успешно обновлена");
    }

    // todo update All
}
