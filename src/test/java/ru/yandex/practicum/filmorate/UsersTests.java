package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
@SpringBootApplication
public class UsersTests {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testNoNameUseLogin() {
        User user = new User();
        user.setName("");
        user.setLogin("k.zimi");
        user.setBirthday(LocalDate.of(1995, 8, 27));
        User userController = new UserController().add(user);

        Assertions.assertEquals(userController.getName(), "k.zimi");
    }

    @Test
    public void testEmail() {
        User user = new User();
        user.setName("Katrin");
        user.setLogin("k.zimi");
        user.setEmail("katrin.mail.ru");
        user.setBirthday(LocalDate.of(1995, 8, 27));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertEquals(violations.size(), 1);
        Assertions.assertEquals("электронная почта должна содержать символ @", violations.iterator().next().getMessage());

        user.setEmail("");
        Set<ConstraintViolation<User>> violations1 = validator.validate(user);

        Assertions.assertEquals(violations.size(), 1);
        Assertions.assertEquals("электронная почта не может быть пустой", violations1.iterator().next().getMessage());
    }

    @Test
    public void testLoginIsNotEmpty() {
        User user = new User();
        user.setName("Katrin");
        user.setLogin("");
        user.setEmail("katrin@mail.ru");
        user.setBirthday(LocalDate.of(1995, 8, 27));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertEquals(violations.size(), 1);
        Assertions.assertEquals("логин не может быть пустым и содержать пробелы", violations.iterator().next().getMessage());
    }

    @Test
    public void testPastBirthday() {
        User user = new User();
        user.setName("Katrin");
        user.setLogin("katrin.zimi");
        user.setEmail("katrin@mail.ru");
        user.setBirthday(LocalDate.of(2025, 8, 27));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        Assertions.assertEquals(violations.size(), 1);
        Assertions.assertEquals("дата рождения не может быть в будущем", violations.iterator().next().getMessage());
    }
}

