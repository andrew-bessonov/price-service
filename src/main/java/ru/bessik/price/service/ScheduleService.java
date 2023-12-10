package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final UpdatePriceService priceService;

    @Scheduled(cron = "0 0 15 * * *")
    public void updatePrice() {
        log.info("Началось обновление всех цен");
        priceService.updateAll();
    }
}
