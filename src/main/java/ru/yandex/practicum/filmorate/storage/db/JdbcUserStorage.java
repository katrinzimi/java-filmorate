package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class JdbcUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        Integer id = user.getId();
        if (id == null) {
            id = jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR USERS_SEQUENCE",
                    (rs, num) -> rs.getInt(1));
        }
        String sqlQuery = "insert into users(id, email, login, name, birthday) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                id,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return selectUserById(id);
    }

    private User selectUserById(Integer userId) {
        String sql = "select * from USERS where id =  ? ";
        List<User> userList = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
        if (userList.isEmpty()) {
            return null;
        }
        return userList.get(0);

    }

    private User makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, email, login, name, birthday);
    }


    @Override
    public User update(User user) {
        String sqlQuery = "update USERS set  email = ?, login =?, name =?, birthday =? where id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return selectUserById(user.getId());
    }

    @Override
    public List<User> getAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "insert into FRIENDS (USER_ID,FRIEND_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "delete from FRIENDS where USER_ID=? and FRIEND_ID=? ";
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        String sql = "select * from USERS where id in ( select friend_id from FRIENDS where user_id = ?) ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int anotherId) {
        Set<Integer> commonFriendsIds = new HashSet<>(getFriendIds(userId));
        commonFriendsIds.retainAll(getFriendIds(anotherId));
        return commonFriendsIds.stream()
                .map(this::selectUserById)
                .collect(Collectors.toList());
    }

    private List<Integer> getFriendIds(int userId) {
        String sql = " select friend_id from FRIENDS where user_id = ? ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt(1), userId);
    }

    @Override
    public User findById(int id) {
        return selectUserById(id);
    }
}
