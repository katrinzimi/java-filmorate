package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.JdbcGenreStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private GenreStorage genreStorage;

    @BeforeEach
    public void init() {
        genreStorage = new JdbcGenreStorage(jdbcTemplate);
    }

    @Test
    public void testGetGenreById() {
        Genre genre = genreStorage.getGenreById(1);

        assertThat(genre)
                .usingRecursiveComparison()
                .isEqualTo(new Genre(1, "Комедия"));
    }

    @Test
    public void testGetGenreAll() {
        List<Genre> genreAll = genreStorage.getGenreAll();

        assertThat(genreAll.size())
                .isEqualTo(6);
    }

}