package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class BaseFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public BaseFilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);
        if (film == null) {
            throw new NotFoundException(String.format("Фиьма с id = %d не существует", filmId));
        }
        if (user == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", userId));
        }
        filmStorage.addLike(filmId, userId);

    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);
        if (film == null) {
            throw new NotFoundException(String.format("Фильма с id = %d не существует", filmId));
        }
        if (user == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", userId));
        }

        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getFilmsPopular(int count) {
        return filmStorage.getFilmsPopular(count);
    }
}
