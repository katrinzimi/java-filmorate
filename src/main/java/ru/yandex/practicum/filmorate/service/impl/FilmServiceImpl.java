package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage, GenreStorage genreStorage, MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public Film create(Film film) {
        checkFilmReferences(film);
        return filmStorage.create(film);
    }

    private void checkFilmReferences(Film film) {
        if (mpaStorage.getMpaById(film.getMpa().getId()) == null) {
            throw new ValidationException("MPA не найден");
        }

        if (!genreStorage.checkGenresExist(film.getGenres().stream()
                .map(Genre::getId).collect(Collectors.toSet()))) {
            throw new ValidationException("Жанр не найден");
        }
    }

    public Film update(Film film) {
        Film film1 = filmStorage.findById(film.getId());
        if (film1 == null) {
            throw new NotFoundException("Такого id не существует");
        }
        checkFilmReferences(film);
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


    @Override
    public Film findById(int id) {
        Film result = filmStorage.findById(id);
        if (result == null) {
            throw new NotFoundException(String.format("Фильма с id = %d не существует", id));
        }
        return result;
    }

}
