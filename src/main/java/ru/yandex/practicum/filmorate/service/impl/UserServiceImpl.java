package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        User user1 = userStorage.findById(user.getId());
        if (user.getId() != null && user1 == null) {
            throw new NotFoundException("Такого id не существует");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.update(user);

    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", userId));
        }
        if (userStorage.findById(friendId) == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", friendId));
        }
        userStorage.addFriend(userId, friendId);

    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", userId));
        }
        if (userStorage.findById(friendId) == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", friendId));
        }
        userStorage.deleteFriend(userId, friendId);

    }

    public List<User> getFriends(int userId) {
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", userId));
        }
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int another) {
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", userId));
        }
        User anotherUser = userStorage.findById(another);
        if (anotherUser == null) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", another));
        }
        return userStorage.getCommonFriends(userId, another);
    }

}

