package ru.bessik.price.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.bessik.price.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByUrl(String url);

    @Query(value = """
                SELECT DISTINCT product.*
                FROM product
                    RIGHT JOIN user_subscriptions ON product.id = user_subscriptions.product_id
            """, nativeQuery = true)
    List<Product> findAllSubscribedUsers();
}
