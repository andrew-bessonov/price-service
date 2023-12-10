package ru.bessik.price.repository;

import org.springframework.data.repository.CrudRepository;
import ru.bessik.price.entity.Product;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByUrl(String url);
}
