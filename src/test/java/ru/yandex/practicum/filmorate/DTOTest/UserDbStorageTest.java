package ru.yandex.practicum.filmorate.DTOTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserDbStorageTest {
    @Autowired
    private UserStorage storage;


    @Test
    void create() {
        User user = new User(
                0L,
                "sergey@mail.ru",
                "serega",
                "Sergey",
                LocalDate.of(1998, 3, 5),
                new HashSet<>()
        );

        assertTrue(storage.saveUser(user).getId() != 0);
    }

    @Test
    void update() {
        User user = new User(
                1L,
                "vasiliy@mail.ru",
                "vasia",
                "Vasiliy",
                LocalDate.of(1999, 9, 9),
                new HashSet<>()
        );

        assertTrue(storage.update(user).getName().equals("Vasiliy"));

        user.setId(300L);

        assertThrows(NotFoundException.class, () -> storage.update(user));
    }

    @Test
    void getById() {

        assertTrue(storage.getUser(1L).isPresent());

        assertFalse(storage.getUser(-50L).isPresent());
    }
}
