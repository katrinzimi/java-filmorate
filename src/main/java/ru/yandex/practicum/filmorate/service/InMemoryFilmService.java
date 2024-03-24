package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@Service
@Slf4j
public class InMemoryFilmService implements FilmService {
    private final InMemoryFilmStorage filmStorage;

    public InMemoryFilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film add(Film film) {
        log.info("Получен запрос");
        Film add = filmStorage.add(film);
        log.info("Фильм создан: " + film);
        return add;
    }

    public Film update(Film film) {
        log.info("Получен запрос");
        Film film1 = filmStorage.findFilm(film.getId());
        if (film.getId() != null && film1 == null) {
            throw new NotFoundException("Такого id не существует");
        }
        Film update = filmStorage.update(film);
        log.info("Фильм обновлен: " + film);
        return update;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addLike(Integer filmId, Integer userId) {
        log.info("Получен запрос");
        Film film = filmStorage.findFilm(filmId);
        List<Film> films = filmStorage.getAll();
        if (film.getId() == null && !films.contains(film)) {
            throw new NotFoundException(String.format("Фиьма с id = %d не существует", filmId));
        }
        Film result = filmStorage.addLike(filmId, userId);
        log.info("Фильм добавлен");
        return result;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        log.info("Получен запрос");
        Film film = filmStorage.deleteLike(filmId, userId);
        log.info("Фильм удален");
        return film;
    }

    public List<Film> getFilmsPopular(Integer count) {
        return filmStorage.getFilmsPopular(count);
    }
}
