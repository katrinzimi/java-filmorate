package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    LinkedHashMap<Integer, Film> films = new LinkedHashMap<>();
    int counter;

    @PostMapping("/films")
    public Film add(@Valid @RequestBody Film film) {
        log.info("Получен запрос");
        counter++;
        films.put(counter, film);
        film.setId(counter);
        log.info("Фильм создан: " + film);
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен зарос");
        if (!films.containsKey(film.getId()) && film.getId() != null) {
            throw new ValidationException("Такого id не существует");
        }
        films.put(film.getId(), film);
        log.info("Фильм обновлен: " + film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Получен зарос");
        LinkedList<Film> result = new LinkedList<>(new ArrayList<>(films.values()));
        log.info("Получен список фильмов: " + result);
        return result;
    }
}

