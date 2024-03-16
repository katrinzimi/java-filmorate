package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        log.info("Получен зарос");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User result = userStorage.addUser(user);
        log.info("Пользователь создан: " + result);
        return result;
    }

    public User updateUser(User user) {
        log.info("Получен зарос");
        User result = userStorage.updateUser(user);
        log.info("Пользователь обновлен: " + result);
        return result;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addFriend(Integer userId, Integer friendId) {
        log.info("Получен зарос");
        userStorage.addFriend(friendId, userId);
        User result = userStorage.addFriend(userId, friendId);
        log.info("Друг добавлен");
        return result;
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        log.info("Получен зарос");
        userStorage.deleteFriend(friendId, userId);
        User result = userStorage.deleteFriend(userId, friendId);
        log.info("Друг удален");
        return result;
    }

    public List<User> getFriends(Integer userId) {
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer another) {
        List<User> commonFriends = userStorage.getCommonFriends(userId, another);
        return userStorage.getCommonFriends(userId, another);
    }

}

