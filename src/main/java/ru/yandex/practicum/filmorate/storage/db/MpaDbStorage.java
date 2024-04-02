package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage extends BaseStorage implements MpaStorage {
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
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
        return queryForObject(sql, (rs, rowNum) -> makeRating(rs), id);
    }

}

