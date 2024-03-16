package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User addFriend(Integer userId, Integer friendId);

    User deleteFriend(Integer friendId, Integer userId);

    List<User> getFriends(Integer userId);

    List<User> getCommonFriends(Integer userId, Integer anotherId);
}
