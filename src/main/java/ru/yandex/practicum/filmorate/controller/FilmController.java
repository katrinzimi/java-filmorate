package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
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
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос");
        Film add = filmService.create(film);
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
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос");
        filmService.addLike(id, userId);
        log.info("Фильм добавлен");
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос");
        filmService.deleteLike(id, userId);
        log.info("Фильм удален");
    }

    @GetMapping("/films/popular")
    public List<Film> filmsPopular(@Positive @RequestParam(required = false, defaultValue = "10") int count) {
        return filmService.getFilmsPopular(count);
    }

    @GetMapping("/mpa")
    public List<Rating> getMpa() {
        log.info("Получен запрос");
        return filmService.getMpaAll();
    }

    @GetMapping("/mpa/{id}")
    public Rating getMpaById(@PathVariable int id) {
        log.info("Получен запрос");
        return filmService.getMpaById(id);
    }

    @GetMapping("/genres")
    public List<Genre> getGenreAll() {
        log.info("Получен запрос");
        return filmService.getGenreAll();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        log.info("Получен запрос");
        return filmService.getGenreById(id);
    }

    @GetMapping("/films/{id}")
    public Film getFilmsId(@PathVariable int id) {
        log.info("Получен запрос");
        return filmService.findById(id);
    }

}

