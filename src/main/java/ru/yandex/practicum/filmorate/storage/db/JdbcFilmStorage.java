package ru.yandex.practicum.filmorate.storage.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Repository
public class JdbcFilmStorage implements FilmStorage {
    private NamedParameterJdbcOperations jdbcOperations;

    public JdbcFilmStorage(NamedParameterJdbcOperations jdbcOperations) {

        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into films(name, description, releaseDate, duration, rating_id) " +
                "values (:name, :description, :releaseDate, :duration, :rating_id)";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("rating_id", film.getMpa() == null ? null : film.getMpa().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(sqlQuery, namedParameters, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        insertFilmGenres(film);

        return selectFilmById(film.getId());
    }

    private void insertFilmGenres(Film film) {
        String sql = "INSERT INTO film_genre(film_id, genre_id) VALUES (:film_id, :genre_id)";
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(film.getGenres().stream()
                .map(genre -> (Map.of("film_id", film.getId(), "genre_id", genre.getId())))
                .collect(Collectors.toList()));

        jdbcOperations.batchUpdate(sql, batch);
    }

    private Film selectFilmById(Integer filmId) {
        String sql = "SELECT f.*," +
                "       r.name AS rating_name," +
                "       ARRAY_AGG(fg.genre_id) AS film_genres_id," +
                "       ARRAY_AGG(g.name) AS film_genres_name" +
                " FROM films AS f" +
                " LEFT JOIN rating AS r ON f.rating_id = r.id" +
                " LEFT JOIN film_genre AS fg ON f.id = fg.film_id" +
                " LEFT JOIN genres AS g ON fg.genre_id = g.id" +
                " WHERE f.id = :id" +
                " GROUP BY f.id;";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", filmId);
        List<Film> result = jdbcOperations.query(sql, namedParameters, (rs, rowNum) -> makeFilm(rs));
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        int duration = rs.getInt("duration");
        Integer ratingId = rs.getInt("rating_id");
        Mpa rating = null;
        if (ratingId > 0) {
            rating = new Mpa(ratingId, rs.getString("rating_name"));
        }
        List<Integer> filmGenresId = Arrays.stream((Object[]) rs.getArray("film_genres_id").getArray())
                .map(genreId -> (Integer) genreId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<String> filmGenresName = Arrays.stream((Object[]) rs.getArray("film_genres_name").getArray())
                .map(genreName -> (String) genreName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Set<Genre> genres = new LinkedHashSet<>();
        for (var i = 0; i < filmGenresId.size() && i < filmGenresName.size(); ++i) {
            genres.add(new Genre(filmGenresId.get(i), filmGenresName.get(i)));
        }
        return new Film(id, name, description, releaseDate, duration, rating, genres);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update FILMS set  name = :name, description =:description," +
                " releaseDate =:releaseDate, duration =:duration, rating_id =:rating_id where id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("rating_id", film.getMpa() == null ? null : film.getMpa().getId())
                .addValue("id", film.getId());
        jdbcOperations.update(sqlQuery, namedParameters);

        String sqlQueryDeleteGenre = "delete from FILM_GENRE where FILM_ID=:id ";
        jdbcOperations.update(sqlQueryDeleteGenre, namedParameters);

        insertFilmGenres(film);

        return selectFilmById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.*," +
                "       r.name AS rating_name," +
                "       ARRAY_AGG(fg.genre_id) AS film_genres_id," +
                "       ARRAY_AGG(g.name) AS film_genres_name" +
                " FROM films AS f" +
                " LEFT JOIN rating AS r ON f.rating_id = r.id" +
                " LEFT JOIN film_genre AS fg ON f.id = fg.film_id" +
                " LEFT JOIN genres AS g ON fg.genre_id = g.id" +
                " GROUP BY f.id;";
        return jdbcOperations.query(sql, new MapSqlParameterSource(), (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "MERGE INTO LIKES(film_id, user_id) values (:film_id, :user_id)";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("film_id", filmId)
                .addValue("user_id", userId);
        jdbcOperations.update(sql, namedParameters);
    }


    @Override
    public void deleteLike(int filmId, int userId) {
        String sql = "delete from LIKES where FILM_ID=:film_id and USER_ID=:user_id ";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("film_id", filmId)
                .addValue("user_id", userId);
        jdbcOperations.update(sql, namedParameters);
    }

    @Override
    public List<Film> getFilmsPopular(int count) {
        String sql = "SELECT FILM_ID FROM LIKES GROUP BY FILM_ID ORDER BY COUNT(*) DESC  limit :count";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("count", count);
        List<Integer> filmIds = jdbcOperations.query(sql, namedParameters, (rs, rowNum) -> rs.getInt("FILM_ID"));
        return filmIds.stream()
                .map(this::selectFilmById)
                .collect(Collectors.toList());
    }

    @Override
    public Film findById(int id) {
        return selectFilmById(id);
    }

}
