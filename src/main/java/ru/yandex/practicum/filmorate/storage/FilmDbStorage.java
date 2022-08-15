package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Optional<Film> getFilm(long filmId) {
        String sql = "select * from films as F " +
                "left join ratings as R on R.rating_id=F.rating_id " +
                "where F.film_id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToFilm, filmId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getAllFilms(){
        String sql = "select * from films as F " +
                "left join ratings as R on R.rating_id=F.rating_id";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        if(rs.getRow() == 0) {
            throw new NotFoundException("Фильм отсутствует!");
        }
            Film film = new Film(
                    rs.getLong("film_id"),
                    rs.getString("film_name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    new Mpa(rs.getInt("rating_id"), rs.getString("rating")),
                    null,
                    null
            );
            film.setGenres(getGenresByFilmId(film.getId()));
            film.setLikes(getLikesByFilmId(film.getId()));
            return film;
        }

    private LinkedHashSet<Genre> getGenresByFilmId(long id) {
        String sql = "select name, id from genres as G " +
                "left join film_genres as FG on FG.genre_id=G.id " +
                "where film_id=?";
        LinkedHashSet<Genre> genres = new LinkedHashSet<>(jdbcTemplate.query(
                sql,
                (rs, num) -> new Genre(rs.getInt("id"), rs.getString("name")),
                id)
        );
        return genres.isEmpty() ? null : genres;
    }

    private Set<Long> getLikesByFilmId(long id) {
        String sql = "select user_id from likes where film_id=?";
        return new HashSet<Long>(
                jdbcTemplate.query(sql, (rs, num) -> rs.getLong("user_id"), id)
        );
    }

    @Override
    public Film saveFilm(Film film){
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("films").usingGeneratedKeyColumns("film_id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("film_name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("rating_id", film.getMpa().getId());
        Number num = jdbcInsert.executeAndReturnKey(parameters);

        film.setId(num.longValue());
        updateGenres(film);
        return film;
        }

    @Override
    public  Film update(Film film){
        if (getFilm(film.getId()).isEmpty()) {
            throw new NotFoundException("Фильм с id" + film.getId() + " не существует!");
        }
        String sql = "update films set " +
                "film_name=?," +
                "description=?," +
                "release_date=?," +
                "duration=?, "+
                "rating_id=? " +
                "where film_id=?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        updateGenres(film);
        updateLikes(film);
        return film;
        }

        private void updateGenres(Film film) {
            String deleteSql = "delete from film_genres where film_id=?";
            String insertSql = "insert into film_genres (film_id, genre_id) values(?,?)";

            jdbcTemplate.update(deleteSql, film.getId());
            if (film.getGenres() != null) {
                film.getGenres().stream()
                        .map(Genre::getId)
                        .forEach(id -> jdbcTemplate.update(insertSql, film.getId(), id));
            }
        }

        private void updateLikes(Film film) {
            String deleteSql = "delete from likes where film_id=?";
            String insertSql = "insert into likes (film_id, user_id) values(?,?)";

            jdbcTemplate.update(deleteSql, film.getId());
            if (film.getLikes() != null) {
                film.getLikes().forEach(id -> jdbcTemplate.update(insertSql, film.getId(), id));
            }
        }

        @Override
        public List<Film> getPopularFilms(int count) {
        String sql = "select F.film_id rate from films as F " +
                "left join likes as L on F.film_id=L.film_id " +
                "group by F.film_id " +
                "order by count(L.film_id) desc " +
                "limit ?";
        List<Long> idList =jdbcTemplate.query(sql, rs -> {
            List<Long> ids =new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getLong("film_id"));
            }
            return ids;
        }, count);
        sql = "select * from films as F " +
                "join ratings as R on R.rating_id=F.rating_id " +
                "where F.film_id in (" + String.join(",", Collections.nCopies(idList.size(), "?")) +")";
                return jdbcTemplate.query(sql, this::mapRowToFilm, idList.toArray());
        }

        public void clear() {
        String sql = "delete from films";
        jdbcTemplate.update(sql);
        }

}
