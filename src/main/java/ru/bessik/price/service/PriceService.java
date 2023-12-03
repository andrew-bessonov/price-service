package ru.bessik.price.service;

import ru.bessik.price.controller.dto.TrackingRequest;
import ru.bessik.price.controller.dto.TrackingResponse;

public interface PriceService {

    TrackingResponse startTracking(TrackingRequest request);

    void updateAll();

    TrackingResponse stopTracking(TrackingRequest request);
}
