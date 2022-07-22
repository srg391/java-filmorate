package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.userexception.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exception.userexception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.userexception.InvalidLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    @Test
    void validateUserLogin() {
        UserController userController = new UserController();
        User user = new User("srg s", "Sergey", 1L, "srg@yandex.ru", LocalDate.of(1987,10,10));
        assertThrows(InvalidLoginException.class, () -> userController.validateUser(user));
    }

    @Test
    void validateUserLoginIsNull() {
        UserController userController = new UserController();
        User user = new User(null, "Sergey", 1L, "srg@yandex.ru", LocalDate.of(1987,10,10));
        assertThrows(InvalidLoginException.class, () -> userController.validateUser(user));
    }

    @Test
    void validateUserNameIsBlank() {
        UserController userController = new UserController();
        User user = new User("srg", "", 1L, "srg@yandex.ru", LocalDate.of(1987,10,10));
        userController.validateUser(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void validateUserNameIsNull() {
        UserController userController = new UserController();
        User user = new User("srg", null, 1L, "srg@yandex.ru", LocalDate.of(1987,10,10));
        userController.validateUser(user);
        assertEquals(user.getName(), user.getLogin());
    }


    @Test
    void validateUserEmail() {
        UserController userController = new UserController();
        User user = new User("srg", "Sergey", 1L, "srg.yandex.ru", LocalDate.of(1987,10,10));
        assertThrows(InvalidEmailException.class, () -> userController.validateUser(user));
    }

    @Test
    void validateUserEmailIsNull() {
        UserController userController = new UserController();
        User user = new User("srg", "Sergey", 1L, null, LocalDate.of(1987,10,10));
        assertThrows(InvalidEmailException.class, () -> userController.validateUser(user));
    }


    @Test
    void validateUserDuration() {
        UserController userController = new UserController();
        User user = new User("srg", "Sergey", 1L, "srg@yandex.ru", LocalDate.MAX);
        assertThrows(InvalidBirthdayException.class, () -> userController.validateUser(user));
    }

    @Test
    void validateUserDurationIsNull() {
        UserController userController = new UserController();
        User user = new User("srg", "Sergey", 1L, "srg@yandex.ru", null);
        assertThrows(InvalidBirthdayException.class, () -> userController.validateUser(user));
    }

}