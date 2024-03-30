package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage implements FilmStorage {
    private JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        Integer id = jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR FILMS_SEQUENCE",
                (rs, num) -> rs.getInt(1));
        String sqlQuery = "insert into films(id, name, description, releaseDate, duration, rating_id) " +
                "values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                id,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        List<Integer> filmGenres = film.getGenres().stream()
                .map(Genre::getId)
                .distinct()
                .collect(Collectors.toList());
        for (Integer genreId : filmGenres) {
            String sqlQueryGenre = "insert into FILM_GENRE(FILM_ID, GENRE_ID) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQueryGenre,
                    id,
                    genreId);
        }
        return selectFilmById(id);
    }

    private Film selectFilmById(Integer filmId) {
        String sql = "select * from FILMS where id =  ? ";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), filmId);

    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        int duration = rs.getInt("duration");
        Integer ratingId = rs.getInt("rating_id");
        String sqlMpa = "select * from RATING where id = ? ";
        Rating rating = null;
        if (ratingId > 0) {
            rating = jdbcTemplate.queryForObject(sqlMpa, (rs1, rowNum) -> makeRating(rs1), ratingId);
        }
        String sql = "select * from GENRES where id IN (select GENRE_ID from FILM_GENRE WHERE FILM_ID = ?)";
        List<Genre> genreList = jdbcTemplate.query(sql, (rs1, rowNum) -> makeGenre(rs1), id);

        return new Film(id, name, description, releaseDate, duration, rating, List.of(), genreList);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");

        return new Rating(id, name);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update FILMS set  name = ?, description =?, releaseDate =?, duration =?, rating_id =? where id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        String sqlQueryDeleteGenre = "delete from FILM_GENRE where FILM_ID=? ";
        jdbcTemplate.update(sqlQueryDeleteGenre,
                film.getId());

        for (Genre genre : film.getGenres()) {
            String sqlQueryGenre = "insert into FILM_GENRE(FILM_ID, GENRE_ID) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQueryGenre,
                    film.getId(),
                    genre.getId());
        }
        ;
        return selectFilmById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        String sql = "select * from FILMS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery = "insert into LIKES (FILM_ID,USER_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery = "delete from LIKES where FILM_ID=? and USER_ID=? ";
        jdbcTemplate.update(sqlQuery,
                filmId,
                userId);

    }

    @Override
    public List<Film> getFilmsPopular(int count) {
        String sql = "SELECT FILM_ID FROM LIKES GROUP BY FILM_ID ORDER BY COUNT(*) DESC  limit ?";
        List<Integer> filmIds = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("FILM_ID"), count);
        return filmIds.stream()
                .map(this::selectFilmById)
                .collect(Collectors.toList());
    }

    @Override
    public Film findById(int id) {
        return selectFilmById(id);
    }

    @Override
    public List<Rating> getMpaAll() {
        String sql = "select * from RATING ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    @Override
    public Rating getMpaById(int id) {
        String sql = "select * from RATING where id =?";
        return queryForObject(sql, (rs, rowNum) -> makeRating(rs), id);
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

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, @Nullable Object... args) throws DataAccessException {
        List<T> results = (List) jdbcTemplate.query((String) sql, (Object[]) args, (ResultSetExtractor) (new RowMapperResultSetExtractor(rowMapper, 1)));
        return DataAccessUtils.singleResult(results);
    }

}

