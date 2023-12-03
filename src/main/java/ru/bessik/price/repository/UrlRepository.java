package ru.bessik.price.repository;

import org.springframework.data.repository.CrudRepository;
import ru.bessik.price.entity.Url;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends CrudRepository<Url, Long> {

    Optional<Url> findByProductUrl(String productUrl);

    List<Url> findAllByIsNeedUpdateIsTrue();
}
