package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;

@RestController("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    HashMap<Integer, Film> films = new HashMap<>();
    int id;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.info("Получен зарос");
        film.setId(id);
        films.put(id, film);
        id += 1;
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен зарос");
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Такого id не существует");
        } else {
            if (film.getReleaseData().isAfter(LocalDate.of(1995, 12, 28))) {
                films.remove(film.getId());
                films.put(film.getId(), film);
            }
        }
        return film;
    }

    @GetMapping
    public HashMap<Integer, Film> getFilms() {
        log.info("Получен зарос");
        return films;
    }
}
