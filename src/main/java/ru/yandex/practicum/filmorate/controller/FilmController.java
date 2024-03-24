package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film add(@Valid @RequestBody Film film) {
        log.info("Получен запрос");
        Film add = filmService.add(film);
        log.info("Фильм создан: " + film);
        return add;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос");
        Film update = filmService.update(film);
        log.info("Фильм обновлен: " + film);
        return update;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getAll();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен запрос");
        Film result = filmService.addLike(id, userId);
        log.info("Фильм добавлен");
        return result;
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен запрос");
        Film film = filmService.deleteLike(id, userId);
        log.info("Фильм удален");
        return film;
    }

    @GetMapping("/films/popular")
    public List<Film> filmsPopular(@Positive @RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getFilmsPopular(count);
    }

}

