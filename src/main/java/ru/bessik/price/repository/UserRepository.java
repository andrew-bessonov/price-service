package ru.bessik.price.repository;

import org.springframework.data.repository.CrudRepository;
import ru.bessik.price.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByTelegramId(String telegramId);
}
