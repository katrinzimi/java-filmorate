package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.JdbcUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    User newUser = new User(1, "user@email.ru", "vanya123",
            "Ivan Petrov", LocalDate.of(1990, 1, 1));
    User updateUser = new User(1, "userUpdate@email.ru", "vanya321",
            "Ivan Ivanov", LocalDate.of(1990, 2, 2));
    User friendUser = new User(2, "userFritnd@email.ru", "vanya555",
            "Ivan Voronov", LocalDate.of(1995, 6, 22));

    @AfterEach
    public void clear() {
        jdbcTemplate.update("delete from FRIENDS");
        jdbcTemplate.update("delete from USERS");
    }

    @Test
    public void testFindUserById() {
        JdbcUserStorage userStorage = new JdbcUserStorage(jdbcTemplate);
        userStorage.create(newUser);

        User savedUser = userStorage.findById(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testUpdateUser() {
        JdbcUserStorage userStorage = new JdbcUserStorage(jdbcTemplate);
        userStorage.create(newUser);
        User update = userStorage.update(updateUser);

        User savedUser = userStorage.findById(1);

        assertThat(update)
                .usingRecursiveComparison()
                .isEqualTo(savedUser);
    }

    @Test
    public void testGetAll() {
        JdbcUserStorage userStorage = new JdbcUserStorage(jdbcTemplate);
        userStorage.create(newUser);
        List<User> all = userStorage.getAll();

        assertThat(all.size())
                .isEqualTo(1);

        assertThat(all)
                .usingRecursiveComparison()
                .isEqualTo(List.of(newUser));
    }

    @Test
    public void testGetFriend() {
        JdbcUserStorage userStorage = new JdbcUserStorage(jdbcTemplate);
        userStorage.create(newUser);
        userStorage.create(friendUser);
        userStorage.addFriend(newUser.getId(), friendUser.getId());

        assertThat(userStorage.getFriends(friendUser.getId()).size())
                .isEqualTo(0);
        assertThat(userStorage.getFriends(newUser.getId()).size())
                .isEqualTo(1);
    }

    @Test
    public void testDeleteFriend() {
        JdbcUserStorage userStorage = new JdbcUserStorage(jdbcTemplate);
        userStorage.create(newUser);
        userStorage.create(friendUser);
        userStorage.addFriend(newUser.getId(), friendUser.getId());
        assertThat(userStorage.getFriends(newUser.getId()).size())
                .isEqualTo(1);
        userStorage.deleteFriend(newUser.getId(), friendUser.getId());

        assertThat(userStorage.getFriends(newUser.getId()).size())
                .isEqualTo(0);
    }

    @Test
    public void testGetCommonFriends() {
        // Подготавливаем данные для теста
        JdbcUserStorage userStorage = new JdbcUserStorage(jdbcTemplate);
        userStorage.create(newUser);
        userStorage.create(friendUser);
        User user3 = userStorage.create(new User(3, "user3@email.ru", "vanya1233",
                "Ivan Petrov3", LocalDate.of(1993, 1, 1)));
        User user4 = userStorage.create(new User(4, "user4@email.ru", "vanya1234",
                "Ivan Petrov4", LocalDate.of(1994, 1, 1)));
        User user5 = userStorage.create(new User(5, "user5@email.ru", "vanya1235",
                "Ivan Petrov5", LocalDate.of(1995, 1, 1)));
        userStorage.addFriend(newUser.getId(), user3.getId());
        userStorage.addFriend(friendUser.getId(), user3.getId());
        userStorage.addFriend(friendUser.getId(), user5.getId());
        userStorage.addFriend(newUser.getId(), user4.getId());
        userStorage.addFriend(newUser.getId(), user5.getId());
        List<User> commonFriends = userStorage.getCommonFriends(newUser.getId(), friendUser.getId());

        assertThat(commonFriends)
                .usingRecursiveComparison()
                .isEqualTo(List.of(user3, user5));
    }

}