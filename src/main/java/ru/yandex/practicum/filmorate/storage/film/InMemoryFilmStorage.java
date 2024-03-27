package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
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
    public Film create(Film film) {
        counter++;
        film.setId(counter);
        films.put(counter, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new LinkedList<>(films.values());
    }

    public void addLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.getLike().add(userId);
        update(film);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.getLike().remove(userId);
        update(film);
    }

    public List<Film> getFilmsPopular(int count) {
        return films.values().stream().sorted(
                        (o1, o2) -> Integer.compare(o2.getLike().size(),
                                o1.getLike().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film findById(int id) {
        return films.get(id);
    }

}

