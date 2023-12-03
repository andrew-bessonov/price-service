package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bessik.price.entity.Url;
import ru.bessik.price.repository.UrlRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final UrlRepository urlRepository;
    private final PriceService priceService;

    @Scheduled(cron = "0 * * * * *")
    public void updatePrice() {
        log.info("Началось обновление всех цен");
        List<Url> urls = urlRepository.findAllByIsNeedUpdateIsTrue();
        priceService.updateAll(urls);
    }
}
