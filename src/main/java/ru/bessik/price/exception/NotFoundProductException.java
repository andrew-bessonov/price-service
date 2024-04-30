package ru.bessik.price.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundProductException extends RuntimeException {
    public NotFoundProductException(String message) {
        super(message);
    }
}
