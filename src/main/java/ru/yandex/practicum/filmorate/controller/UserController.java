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
    int id;

    @PostMapping("/users")
    public User add(@Valid @RequestBody User user) {
        log.info("Получен зарос");
        id += 1;
        users.put(id, user);
        user.setId(id);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Получен зарос");
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Такого id не существует");
        } else {
            users.remove(user.getId());
            users.put(user.getId(), user);
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
        }
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Получен зарос");
        return new ArrayList<>(users.values());
    }
}
