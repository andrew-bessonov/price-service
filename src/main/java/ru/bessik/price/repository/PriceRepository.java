package ru.bessik.price.repository;

import org.springframework.data.repository.CrudRepository;
import ru.bessik.price.entity.Price;

public interface PriceRepository extends CrudRepository<Price, Long> {
}
