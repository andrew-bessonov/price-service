package ru.bessik.price.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bessik.price.entity.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByUrl(String url);

    @Query(value = """
        SELECT DISTINCT p.*
        FROM product p
                 RIGHT JOIN user_subscriptions s ON p.id = s.product_id
        """, nativeQuery = true)
    List<Product> findAllSubscribedProduct();
}
