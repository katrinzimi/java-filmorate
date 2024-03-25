package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@Service
@Slf4j
public class BaseFilmService implements FilmService {
    private final InMemoryFilmStorage filmStorage;

    public BaseFilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film create(Film film) {
        Film add = filmStorage.create(film);
        return add;
    }

    public Film update(Film film) {
        Film film1 = filmStorage.findById(film.getId());
        if (film1 == null) {
            throw new NotFoundException("Такого id не существует");
        }
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findById(filmId);
        if (film.getId() == null) {
            throw new NotFoundException(String.format("Фиьма с id = %d не существует", filmId));
        }
        Film result = filmStorage.addLike(filmId, userId);
        return result;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.deleteLike(filmId, userId);
        return film;
    }

    public List<Film> getFilmsPopular(Integer count) {
        return filmStorage.getFilmsPopular(count);
    }
}
