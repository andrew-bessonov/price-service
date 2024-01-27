package ru.bessik.price.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bessik.price.utils.Utils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdatePriceServiceFactory {

    private final List<UpdatePriceService> serviceList;

    public UpdatePriceService getService(String url) {
        String domainSite = Utils.getDomainSiteFromUrl(url);
        for (UpdatePriceService service : serviceList) {
            if (service.getSiteUrl().equals(domainSite)) {
                return service;
            }
        }
        throw new RuntimeException("site is not supported");
    }
}
