package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class GenreDbStorage extends BaseStorage implements GenreStorage {

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }

    @Override
    public List<Genre> getGenreAll() {
        String sql = "select * from GENRES ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "select * from GENRES where id =? ";
        return queryForObject(sql, (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public boolean checkGenresExist(Set<Integer> genres) {
        String sql = String.format("select ID from GENRES where id in (%s)", genres.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")));
        List<Integer> genreIds = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("ID"));
        return genres.size() == genreIds.size();
    }

}

