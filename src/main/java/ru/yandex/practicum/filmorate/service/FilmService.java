package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    List<Film> getFilmsPopular(Integer count);

}
