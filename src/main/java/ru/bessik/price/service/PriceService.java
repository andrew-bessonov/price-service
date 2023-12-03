package ru.bessik.price.service;

import ru.bessik.price.controller.dto.StartTrackingRequest;
import ru.bessik.price.controller.dto.StartTrackingResponse;

public interface PriceService {

    StartTrackingResponse startTracking(StartTrackingRequest request);
}
