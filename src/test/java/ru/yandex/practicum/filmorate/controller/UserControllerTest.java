package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.userexception.InvalidLoginException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    @Test
    void validateUserLogin() {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        User user = new User("srg s", "Sergey", 1L, "srg@yandex.ru", LocalDate.of(1987,10,10), new HashSet<>());
        assertThrows(InvalidLoginException.class, () -> userController.validateUser(user));
    }

    @Test
    void validateUserNameIsBlank() {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        User user = new User("srg", "", 1L, "srg@yandex.ru", LocalDate.of(1987,10,10), new HashSet<>());
        userController.validateUser(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void validateUserNameIsNull() {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        User user = new User("srg", null, 1L, "srg@yandex.ru", LocalDate.of(1987,10,10), new HashSet<>());
        userController.validateUser(user);
        assertEquals(user.getName(), user.getLogin());
    }
}