package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    FilmDbStorage filmStorage;
    UserDbStorage userDbStorage;
    Film newFilm = new Film(1, "film1", "description",
            LocalDate.of(2010, 1, 22), 15,
            new Rating(1, "G"), List.of(), List.of(new Genre(1, "Комедия")));
    Film updateFilm = new Film(1, "film2", "description2",
            LocalDate.of(2012, 1, 22), 12,
            new Rating(2, "PG"), List.of(), List.of(new Genre(2, "Драма")));

    @BeforeEach
    public void init() {
        filmStorage = new FilmDbStorage(jdbcTemplate);
        userDbStorage = new UserDbStorage(jdbcTemplate);
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
        filmStorage.create(newFilm);

        Film savedFilm = filmStorage.findById(1);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testUpdateUser() {
        filmStorage.create(newFilm);
        Film update = filmStorage.update(updateFilm);

        Film savedUser = filmStorage.findById(1);

        assertThat(update)
                .usingRecursiveComparison()
                .isEqualTo(savedUser);
    }

    @Test
    public void testGetAll() {
        filmStorage.create(newFilm);
        List<Film> all = filmStorage.getAll();

        assertThat(all)
                .usingRecursiveComparison()
                .isEqualTo(List.of(newFilm));
    }

    @Test
    public void testAddLike() {
        Film film = filmStorage.create(newFilm);
        User user = userDbStorage.create(new User(1, "user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1)));
        filmStorage.addLike(film.getId(), user.getId());
        Film savedFilm = filmStorage.findById(1);

        assertThat(savedFilm.getLikes())
                .usingRecursiveComparison()
                .isEqualTo(List.of(user.getId()));
    }

    @Test
    public void testDeleteLike() {
        Film film = filmStorage.create(newFilm);
        User user = userDbStorage.create(new User(1, "user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1)));
        filmStorage.addLike(film.getId(), user.getId());
        filmStorage.deleteLike(film.getId(), user.getId());
        Film savedFilm = filmStorage.findById(1);

        assertThat(savedFilm.getLikes())
                .usingRecursiveComparison()
                .isEqualTo(List.of());
    }

    @Test
    public void testGetFilmsPopular() {
        Film film1 = filmStorage.create(newFilm);
        User user1 = userDbStorage.create(new User(1, "user@email.ru", "vanya123",
                "Ivan Petrov", LocalDate.of(1990, 1, 1)));

        Film film2 = filmStorage.create(new Film(2, "film2", "description2",
                LocalDate.of(2012, 1, 22), 25,
                new Rating(1, "G"), List.of(), List.of(new Genre(1, "Комедия"))));

        User user2 = userDbStorage.create(new User(2, "user2@email.ru", "vanya2",
                "Ivan Petrov2", LocalDate.of(1992, 1, 1)));

        Film film3 = filmStorage.create(new Film(3, "film3", "description3",
                LocalDate.of(2013, 1, 22), 35,
                new Rating(1, "G"), List.of(), List.of(new Genre(1, "Комедия"))));

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

    @Test
    public void testGetMpaById() {
        Rating mpa = filmStorage.getMpaById(1);

        assertThat(mpa)
                .usingRecursiveComparison()
                .isEqualTo(new Rating(1, "G"));
    }

    @Test
    public void testGetGenreById() {
        Genre genre = filmStorage.getGenreById(1);

        assertThat(genre)
                .usingRecursiveComparison()
                .isEqualTo(new Genre(1, "Комедия"));
    }

    @Test
    public void testGetMpaAll() {
        List<Rating> mpaAll = filmStorage.getMpaAll();

        assertThat(mpaAll.size())
                .isEqualTo(5);
    }

    @Test
    public void testGetGenreAll() {
        List<Genre> genreAll = filmStorage.getGenreAll();

        assertThat(genreAll.size())
                .isEqualTo(6);
    }
}