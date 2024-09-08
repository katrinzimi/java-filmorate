package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.db.JdbcMpaStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private MpaStorage mpaStorage;

    @BeforeEach
    public void init() {
        mpaStorage = new JdbcMpaStorage(jdbcTemplate);
    }

    @Test
    public void testGetMpaById() {
        Mpa mpa = mpaStorage.getMpaById(1);

        assertThat(mpa)
                .usingRecursiveComparison()
                .isEqualTo(new Mpa(1, "G"));
    }

    @Test
    public void testGetMpaAll() {
        List<Mpa> mpaAll = mpaStorage.getMpaAll();

        assertThat(mpaAll.size())
                .isEqualTo(5);
    }
}