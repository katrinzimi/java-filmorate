package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage filmStorage) {
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
        Film update = filmStorage.update(film);
        log.info("Фильм обновлен: " + film);
        return update;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addLike(Integer filmId, Integer userId) {
        log.info("Получен запрос");
        Film film = filmStorage.addLike(filmId, userId);
        log.info("Фильм добавлен");
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        log.info("Получен запрос");
        Film film = filmStorage.deleteLike(filmId, userId);
        log.info("Фильм удален");
        return film;
    }

    public List<Film> getFilmsPopular(Integer count) {
        List<Film> filmsPopular = filmStorage.getFilmsPopular(count);
        return filmsPopular;
    }
}
