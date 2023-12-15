package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    HashMap<Integer, Film> films = new HashMap<>();
    int id;

    @PostMapping("/films")
    public Film add(@Valid @RequestBody Film film) {
        log.info("Получен запрос");
        id += 1;
        films.put(id, film);
        film.setId(id);
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен зарос");
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Такого id не существует");
        } else {
            films.remove(film.getId());
            films.put(film.getId(), film);
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Получен зарос");
        ArrayList<Film> result = new ArrayList<>(films.values());
        result.sort(Comparator.comparingInt(Film::getId));
        return result;
    }
}
