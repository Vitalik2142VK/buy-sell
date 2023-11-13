package ru.skypro.homework.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skypro.homework.exception.NotFoundAnnounceException;
import ru.skypro.homework.exception.NotFoundUserException;

import java.util.Arrays;

@RestControllerAdvice
public class ExceptionsHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({NotFoundAnnounceException.class, NotFoundUserException.class})
    public void notFoundAnnounceExceptionHandler(RuntimeException exception) {
        if (exception.getClass().equals(NotFoundAnnounceException.class)) {
            log.error("Announce!!! Processing of the announce interrupted in this part of the code: " + Arrays.toString(exception.getStackTrace()));
        } else {
            log.error("User!!! Processing of the user interrupted in this part of the code: " + Arrays.toString(exception.getStackTrace()));
        }
    }
}
