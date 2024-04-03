package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcMpaStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Mpa makeRating(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");

        return new Mpa(id, name);
    }

    @Override
    public List<Mpa> getMpaAll() {
        String sql = "select * from RATING ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    @Override
    public Mpa getMpaById(int id) {
        String sql = "select * from RATING where id =?";
        List<Mpa> result = jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs), id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

}

