package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getFilmsPopular(int count);

    Film findById(int id);

    List<Rating> getMpaAll();

    Rating getMpaById(int id);

    List<Genre> getGenreAll();

    Genre getGenreById(int id);

    List<Integer> getLikedUsersByFilmId(int id);

    boolean checkGenresExist(Set<Integer> genres);
}