package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.lang.Nullable;

import java.util.List;

public class BaseStorage {
    protected final JdbcTemplate jdbcTemplate;

    public BaseStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected  <T> T queryForObject(String sql, RowMapper<T> rowMapper, @Nullable Object... args) throws DataAccessException {
        List<T> results = (List) jdbcTemplate.query((String) sql, (Object[]) args, (ResultSetExtractor) (new RowMapperResultSetExtractor(rowMapper, 1)));
        return DataAccessUtils.singleResult(results);
    }
}
