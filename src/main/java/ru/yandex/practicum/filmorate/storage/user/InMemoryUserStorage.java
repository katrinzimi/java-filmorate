package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    HashMap<Integer, User> users = new HashMap<>();
    int counter;

    @Override
    public User create(User user) {
        counter++;
        user.setId(counter);
        users.put(counter, user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User addFriend(Integer userId, Integer friendId) {
        User user = users.get(userId);
        user.getFriends().add(friendId);
        update(user);
        return user;
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        User user = users.get(userId);
        user.getFriends().remove(friendId);
        return user;
    }

    @Override
    public List<User> getFriends(Integer userId) {
        User user = users.get(userId);
        return user.getFriends().stream()
                .map(friendId -> users.get(friendId))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer anotherId) {
        User user = users.get(userId);
        User anotherUser = users.get(anotherId);
        Set<Integer> commonFriendsIds = new HashSet<>(user.getFriends());
        commonFriendsIds.retainAll(anotherUser.getFriends());
        return commonFriendsIds.stream()
                .map(friendId -> users.get(friendId))
                .collect(Collectors.toList());
    }

    @Override
    public User findById(Integer id) {
        return users.get(id);
    }

}
