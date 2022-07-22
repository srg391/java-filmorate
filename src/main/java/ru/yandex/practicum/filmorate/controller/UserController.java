package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.userexception.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long idCounter = 0;

    public long generateNewId() {
        return ++idCounter;
    }

    @GetMapping
    public List<User> findAllUsers() {
        log.debug("Количество постов :" + users.size());
        List<User> usersList = new ArrayList<>(users.values());
        return usersList;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (users.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован!");
        }
        user.setId(generateNewId());
        log.debug("Добавлен пользователь :" + user);
        validateUser(user);
        save(user);
        return user;
    }

    void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new InvalidEmailException("Адрес электронной почты не соотвествует!");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new InvalidLoginException("Логин не соотвествует!");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidBirthdayException("Дата рождения не соотвествует!");
        }
    }

    void save(User user) {
        users.put(user.getId(), user);
    }

    @PutMapping
    public User putUser(@RequestBody User user) {
        if (user.getId() == null) {
            throw new InvalidIdUserException("Id пользователя не может быть пустым!");
        } else if (!users.containsKey(user.getId())) {
            throw new InvalidIdUserException("Пользователь не существует!");
        }
        log.debug("Изменен пользователь :" + user);
        validateUser(user);
        save(user);
        return user;
    }
}

