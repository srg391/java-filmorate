package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Primary
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "select * from ratings";
        return jdbcTemplate.query(sql, this::mpaRowToMpa);
    }

    @Override
    public Optional<Mpa> findById(int id) {
        String sql = "select * from ratings where rating_id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mpaRowToMpa, id));
        }catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Mpa mpaRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        if (rs.getRow() == 0) {
            throw new NotFoundException("Рейтинг не существует!");
        }
        return new Mpa(rs.getInt("rating_id"), rs.getString("rating"));
    }

}
