package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    HashMap<Integer, User> users = new HashMap<>();
    int counter;

    @Override
    public User addUser(User user) {
        counter++;
        user.setId(counter);
        users.put(counter, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() != null && !users.containsKey(user.getId())) {
            throw new ValidationException("Такого id не существует");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User addFriend(Integer userId, Integer friendId) {
        User user = users.get(userId);
        if (user == null) {
            throw new ValidationException(String.format("Пользователя с id = %d не существует", userId));
        }
        if (!users.containsKey(friendId)) {
            throw new ValidationException(String.format("Пользователя с id = %d не существует", friendId));
        }
        user.getFriends().add(friendId);
        updateUser(user);
        return user;
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = users.get(userId);
        if (user == null) {
            throw new ValidationException(String.format("Пользователя с id = %d не существует", userId));
        }
        if (!users.containsKey(friendId)) {
            throw new ValidationException(String.format("Пользователя с id = %d не существует", friendId));
        }
        user.getFriends().remove(friendId);
        updateUser(user);
        return user;
    }

    @Override
    public List<User> getFriends(Integer userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new ValidationException(String.format("Пользователя с id = %d не существует", userId));
        }
        return user.getFriends().stream()
                .map(friendId -> users.get(friendId))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer anotherId) {
        User user = users.get(userId);
        if (user == null) {
            throw new ValidationException(String.format("Пользователя с id = %d не существует", userId));
        }
        User anotherUser = users.get(anotherId);
        if (anotherUser == null) {
            throw new ValidationException(String.format("Пользователя с id = %d не существует", anotherId));
        }
        Set<Integer> commonFriendsIds = new HashSet<>(user.getFriends());
        commonFriendsIds.retainAll(anotherUser.getFriends());
        return commonFriendsIds.stream()
                .map(friendId -> users.get(friendId))
                .collect(Collectors.toList());
    }

}
