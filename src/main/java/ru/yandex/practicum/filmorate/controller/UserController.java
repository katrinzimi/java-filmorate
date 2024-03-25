package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Получен зарос");
        User result = userService.create(user);
        log.info("Пользователь создан: " + result);
        return result;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Получен зарос");
        User result = userService.update(user);
        log.info("Пользователь обновлен: " + result);
        return result;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAll();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен зарос");
        User result = userService.addFriend(id, friendId);
        log.info("Друг добавлен" + result);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен зарос");
        User result = userService.deleteFriend(id, friendId);
        log.info("Друг удален" + result);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
