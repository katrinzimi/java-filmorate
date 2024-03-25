package ru.yandex.practicum.filmorate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationException e) {
        log.warn("Ошибка 400 при обработке запроса");
        return new ErrorResponse(
                "Ошибка с параметром count.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.warn("Ошибка 404 при обработке запроса");
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }

}
