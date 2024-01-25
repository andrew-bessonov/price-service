package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bessik.price.entity.Product;

@Slf4j
@Service
@RequiredArgsConstructor
public class DnsService implements UpdatePriceService {

    @Override
    public String getSiteUrl() {
        return "www.dns-shop.ru";
    }

    @Override
    public void update(Product product) {
        log.warn("site is not supported, yet");
    }
}
