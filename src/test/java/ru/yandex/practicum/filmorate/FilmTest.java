package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
class FilmTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testReleaseData() {
        Film film = new Film();
        film.setName("Титаник");
        film.setDescription("про любовь");
        film.setReleaseDate(LocalDate.of(1894, 12, 14));
        film.setDuration(30);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        Assertions.assertEquals(violations.size(), 1);
        Assertions.assertEquals("дата релиза — не раньше 28 декабря 1895 года", violations.iterator().next().getMessage());
    }

    @Test
    public void testNotName() {
        Film film1 = new Film();
        film1.setName("");
        film1.setDescription("про любовь");
        film1.setReleaseDate(LocalDate.of(1996, 12, 14));
        film1.setDuration(30);
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);

        Assertions.assertEquals(violations.size(), 1);
        Assertions.assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    public void testDescriptionMax200() {
        Film film1 = new Film();
        film1.setName("Титаник");
        film1.setDescription("про любовь про любовь про любовь про любовь про любовь про любовь про любовь про любовь" +
                "про любовь про любовь про любовь про любовь про любовь про любовь про любовь про любовь" +
                "про любовь про любовь про любовь про любовь про любовь про любовь про любовь про любовь");
        film1.setReleaseDate(LocalDate.of(1996, 12, 14));
        film1.setDuration(30);
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);

        Assertions.assertEquals(violations.size(), 1);
        Assertions.assertEquals("максимальная длина описания — 200 символов", violations.iterator().next().getMessage());
    }

    @Test
    public void testPositiveOrZeroDuration() {
        Film film1 = new Film();
        film1.setName("Титаник");
        film1.setDescription("про любовь");
        film1.setReleaseDate(LocalDate.of(1996, 12, 14));
        film1.setDuration(-30);
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);

        Assertions.assertEquals(violations.size(), 1);
        Assertions.assertEquals("продолжительность фильма должна быть положительной", violations.iterator().next().getMessage());
    }
}

