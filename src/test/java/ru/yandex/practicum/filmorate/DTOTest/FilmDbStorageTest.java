package ru.yandex.practicum.filmorate.DTOTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmDbStorageTest {
    @Autowired
    private FilmStorage storage;


    @Test
    void createNewFilmTest() {
        Film film = new Film(
                0L,
                "Pirates",
                "About pirates",
                LocalDate.of(1986, 5, 8),
                110,
                new Mpa(3),
                new HashSet<>(),
                new LinkedHashSet<>()
        );

        assertTrue(storage.saveFilm(film).getId() != 0);
    }

    @Test
    void updateExistingFilmTest() {
        Film film = new Film(
                1L,
                "Pirates New",
                "About asian pirates",
                LocalDate.of(2014, 8, 6),
                120,
                new Mpa(4),
                new HashSet<>(),
                new LinkedHashSet<>()
        );

        assertTrue(storage.update(film).getName().equals("Pirates New"));

        film.setId(300L);

        assertThrows(NotFoundException.class, () -> storage.update(film));
    }

    @Test
    void getFilmByIdTest() {

        assertTrue(storage.getFilm(1L).isPresent());

        assertFalse(storage.getFilm(300L).isPresent());
    }

}
