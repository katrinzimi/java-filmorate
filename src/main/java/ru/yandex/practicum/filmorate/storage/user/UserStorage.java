package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> getAll();

    User addFriend(int userId, int friendId);

    User deleteFriend(int friendId, int userId);

    List<User> getFriends(int userId);

    List<User> getCommonFriends(int userId, int anotherId);

    User findById(int id);
}
