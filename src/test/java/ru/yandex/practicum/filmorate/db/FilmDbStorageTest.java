package ru.yandex.practicum.filmorate.db;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.JdbcFilmStorage;
import ru.yandex.practicum.filmorate.storage.db.JdbcUserStorage;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;
    JdbcFilmStorage filmStorage;
    JdbcUserStorage userDbStorage;
    Film newFilm = new Film(1, "film1", "description",
            LocalDate.of(2010, 1, 22), 15,
            new Mpa(1, "G"), Set.of(new Genre(1, "Комедия")));
    Film updateFilm = new Film(1, "film2", "description2",
            LocalDate.of(2012, 1, 22), 12,
            new Mpa(2, "PG"), Set.of(new Genre(2, "Драма")));

    @BeforeEach
    public void init() {
        filmStorage = new JdbcFilmStorage(jdbcOperations);
        userDbStorage = new JdbcUserStorage(jdbcTemplate);
    }

    @AfterEach
    public void clear() {
        jdbcTemplate.update("delete from FILM_GENRE");
        jdbcTemplate.update("delete from LIKES");
        jdbcTemplate.update("delete from USERS");
        jdbcTemplate.update("delete from FILMS");
    }

    @Test
    public void testFindFilmById() {
        Film savedFilm = filmStorage.create(newFilm);
        Film film = filmStorage.findById(savedFilm.getId());

        assertThat(film)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testUpdateUser() {
        Film savedFilm = filmStorage.create(newFilm);
        updateFilm.setId(savedFilm.getId());
        Film update = filmStorage.update(updateFilm);

        savedFilm = filmStorage.findById(savedFilm.getId());

        assertThat(update)
                .usingRecursiveComparison()
                .isEqualTo(savedFilm);
    }

    @Test
    public void testGetAll() {
        Film savedFilm = filmStorage.create(newFilm);
        List<Film> all = filmStorage.getAll();

        assertThat(all)
                .usingRecursiveComparison()
                .isEqualTo(List.of(savedFilm));
    }

    @Test
    public void testGetFilmsPopular() {
        Film film1 = filmStorage.create(newFilm);
        User user1 = userDbStorage.create(new User(1, "user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1)));

        Film film2 = filmStorage.create(new Film(2, "film2", "description2",
                LocalDate.of(2012, 1, 22), 25,
                new Mpa(1, "G"), Set.of(new Genre(1, "Комедия"))));

        User user2 = userDbStorage.create(new User(2, "user2@email.ru", "vanya2",
                "Ivan Petrov2", LocalDate.of(1992, 1, 1)));

        Film film3 = filmStorage.create(new Film(3, "film3", "description3",
                LocalDate.of(2013, 1, 22), 35,
                new Mpa(1, "G"), Set.of(new Genre(1, "Комедия"))));

        User user3 = userDbStorage.create(new User(3, "user3@email.ru", "vanya3",
                "Ivan Petrov3", LocalDate.of(1993, 1, 1)));

        filmStorage.addLike(film1.getId(), user1.getId());
        filmStorage.addLike(film2.getId(), user1.getId());
        filmStorage.addLike(film3.getId(), user3.getId());
        filmStorage.addLike(film3.getId(), user2.getId());
        filmStorage.addLike(film3.getId(), user1.getId());
        filmStorage.addLike(film1.getId(), user3.getId());
        List<Film> filmsPopular = filmStorage.getFilmsPopular(5);

        assertThat(filmsPopular.stream().map(Film::getId).collect(Collectors.toList()))
                .usingRecursiveComparison()
                .isEqualTo(List.of(film3.getId(), film1.getId(), film2.getId()));
    }
}