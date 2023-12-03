package ru.bessik.price.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bessik.price.service.PriceService;
import ru.bessik.price.controller.dto.TrackingRequest;
import ru.bessik.price.controller.dto.TrackingResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PriceController {

    private final PriceService priceService;

    @PostMapping("/track")
    public TrackingResponse startTracking(@RequestBody TrackingRequest request) {
        log.info("[API] start tracking {}", request);
        return priceService.startTracking(request);
    }

    @PostMapping("/track/stop")
    public TrackingResponse stopTracking(@RequestBody TrackingRequest request) {
        log.info("[API] stop tracking {}", request);
        return priceService.stopTracking(request);
    }
}
