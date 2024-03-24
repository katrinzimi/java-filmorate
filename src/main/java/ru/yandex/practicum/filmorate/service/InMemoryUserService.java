package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class InMemoryUserService implements UserService {
    private final UserStorage userStorage;

    public InMemoryUserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        log.info("Получен зарос");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User result = userStorage.add(user);
        log.info("Пользователь создан: " + result);
        return result;
    }

    public User update(User user) {
        log.info("Получен зарос");
        User user1 = userStorage.findUser(user.getId());
        if (user.getId() != null && user1 == null) {
            throw new NotFoundException("Такого id не существует");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User result = userStorage.update(user);
        log.info("Пользователь обновлен: " + result);
        return result;

    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User addFriend(Integer userId, Integer friendId) {
        log.info("Получен зарос");
        User user = userStorage.findUser(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", userId));
        }
        if (userStorage.findUser(friendId) == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", friendId));
        }
        userStorage.addFriend(friendId, userId);
        User result = userStorage.addFriend(userId, friendId);
        log.info("Друг добавлен");
        return result;
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        log.info("Получен зарос");
        User user = userStorage.findUser(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", userId));
        }
        if (userStorage.findUser(friendId) == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", friendId));
        }
        userStorage.deleteFriend(friendId, userId);
        User result = userStorage.deleteFriend(userId, friendId);
        log.info("Друг удален");
        return result;
    }

    public List<User> getFriends(Integer userId) {
        User user = userStorage.findUser(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", userId));
        }
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer another) {
        User user = userStorage.findUser(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", userId));
        }
        User anotherUser = userStorage.findUser(another);
        if (anotherUser == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", another));
        }
        return userStorage.getCommonFriends(userId, another);
    }

    @Override
    public User findUser(Integer id) {
        return userStorage.findUser(id);
    }

}

