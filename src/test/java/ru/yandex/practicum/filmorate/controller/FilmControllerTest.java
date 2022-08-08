package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.filmexception.InvalidDescriptionException;
import ru.yandex.practicum.filmorate.exception.filmexception.InvalidReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void validateFilmDescriptionIsOverSymbols() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, "Pirates", "200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200", LocalDate.of(1986, 5, 8), 124, new HashSet<>());
        assertThrows(InvalidDescriptionException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void validateFilmReleaseDate() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, "Pirates", "about pirates", LocalDate.of(1894, 5, 8), 124,new HashSet<>());
        assertThrows(InvalidReleaseDateException.class, () -> filmController.validateFilm(film));
    }

}