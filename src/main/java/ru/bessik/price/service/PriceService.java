package ru.bessik.price.service;

import ru.bessik.price.controller.dto.TrackingRequest;
import ru.bessik.price.controller.dto.TrackingResponse;
import ru.bessik.price.entity.Url;

import java.util.List;

public interface PriceService {

    TrackingResponse startTracking(TrackingRequest request);

    void updateAll(List<Url> urls);

    TrackingResponse stopTracking(TrackingRequest request);
}
