package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    HashMap<Integer, User> users = new HashMap<>();
    int counter;

    @PostMapping("/users")
    public User add(@Valid @RequestBody User user) {
        log.info("Получен зарос");
        counter++;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(counter);
        users.put(counter, user);
        log.info("Пользователь создан: " + user);
        return user;

    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Получен зарос");
        if (user.getId() != null && !users.containsKey(user.getId())) {
            throw new ValidationException("Такого id не существует");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь обновлен: " + user);
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Получен зарос");
        ArrayList<User> result = new ArrayList<>(users.values());
        log.info("Получен список пользователей: " + result);
        return result;
    }
}
