package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);

    User update(User user);

    List<User> getAll();

    User addFriend(Integer userId, Integer friendId);

    User deleteFriend(Integer friendId, Integer userId);

    List<User> getFriends(Integer userId);

    List<User> getCommonFriends(Integer userId, Integer anotherId);

    User findUser(Integer id);
}
