package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectCountException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {
    private final FilmService filmServer;
    Integer count10 = 10;

    public FilmController(FilmService filmServer) {
        this.filmServer = filmServer;
    }

    @PostMapping("/films")
    public Film add(@Valid @RequestBody Film film) {
        return filmServer.add(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmServer.update(film);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmServer.getFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmServer.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return filmServer.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> filmsPopular(@RequestParam(required = false) Integer count) throws IncorrectCountException {
        if (count == null) {
            count = count10;
        }
        if (count <= 0) {
            throw new IncorrectCountException("Параметр count имеет отрицательное значение.");
        }
        return filmServer.getFilmsPopular(count);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final IncorrectCountException e) {
        return new ErrorResponse(
                "Ошибка с параметром count.", e.getMessage()
        );
    }
}

