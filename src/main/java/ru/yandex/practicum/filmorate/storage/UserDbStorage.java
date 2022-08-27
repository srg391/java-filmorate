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
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }
    @Override
    public Optional<User> getUser(long id) {
        String sql = "select * from users where user_id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToUser, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    @Override
    public  User saveUser(User user){
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("users").usingGeneratedKeyColumns("user_id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("user_name", user.getName())
                .addValue("birthday", user.getBirthday());

        Number num = jdbcInsert.executeAndReturnKey(parameters);

        user.setId(num.longValue());
        return user;
    }

    @Override
    public User update(User user) {
        if (getUser(user.getId()).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не существует!");
        }
        String sql = "update users set " +
                "email=?," +
                "login=?," +
                "user_name=?," +
                "birthday=? " +
                "where user_id=?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
                updateFriends(user);
                return user;
    }

    private void updateFriends(User user) {
        String deleteSql = "delete from friends where user_id=?";
        String insertSql = "insert into friends (user_id, friend_id) " +
                "values (?,?)";

        jdbcTemplate.update(deleteSql, user.getId());
        if (user.getFriendIds() != null) {
            user.getFriendIds().forEach(id -> jdbcTemplate.update(insertSql, user.getId(), id));
        }
    }
    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        if(rs.getRow() == 0) {
            throw new NotFoundException("user not found");
        }
        User user = new User(
                rs.getLong("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate(),
                null
        );
        user.setFriendIds(getFriendsByUserId(user.getId()));
        return user;
    }

    private Set<Long> getFriendsByUserId(long id) {
        String sql = "select friend_id from friends where user_id=?";
        return  new HashSet<>(
                jdbcTemplate.query(sql, (rs, num) -> rs.getLong("friend_id"), id)
        );
    }

    @Override
    public void clear() {
        String sql = "delete from users";
        jdbcTemplate.update(sql);
    }
}
