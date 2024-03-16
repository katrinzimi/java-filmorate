package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    LinkedHashMap<Integer, Film> films = new LinkedHashMap<>();
    int counter;

    @Override
    public Film add(Film film) {
        counter++;
        film.setId(counter);
        films.put(counter, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() != null && !films.containsKey(film.getId())) {
            throw new ValidationException("Такого id не существует");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new LinkedList<>(films.values());
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = films.get(filmId);
        if (film == null && !films.containsKey(filmId)) {
            throw new ValidationException(String.format("Фиьма с id = %d не существует", filmId));
        }
        assert film != null;
        film.getLike().add(userId);
        update(film);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = films.get(filmId);
        film.getLike().remove(userId);
        update(film);
        return film;
    }

    public List<Film> getFilmsPopular(Integer count) {
        return films.keySet().stream().sorted(
                        (o1, o2) -> Integer.compare(films.get(o2).getLike().size(),
                                films.get(o1).getLike().size()))
                .map(filmLike -> films.get(filmLike))
                .limit(count)
                .collect(Collectors.toList());
    }

}

