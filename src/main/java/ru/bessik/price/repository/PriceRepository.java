package ru.bessik.price.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bessik.price.entity.Price;

@Repository
public interface PriceRepository extends CrudRepository<Price, Long> {
}
