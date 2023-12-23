package ru.bessik.price.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bessik.price.controller.dto.PriceDto;
import ru.bessik.price.controller.dto.PriceRequest;
import ru.bessik.price.entity.Price;
import ru.bessik.price.entity.Product;
import ru.bessik.price.repository.PriceRepository;
import ru.bessik.price.utils.PriceMapper;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceService {

    private final ProductService productService;
    private final PriceRepository priceRepository;

    @Transactional
    public List<PriceDto> getPrices(PriceRequest request) {
        LocalDate startDate = LocalDate.now().minusDays(request.getPeriodInDays());
        Product product = productService.findByUrl(request.getProductUrl())
                .orElseThrow();
        return product.getPrices().stream()
                .filter(it -> it.getPriceDate().isAfter(startDate))
                .map(PriceMapper::toDto)
                .toList();
    }

    @Transactional
    public Price save(Price price) {
        return priceRepository.save(price);
    }
}
