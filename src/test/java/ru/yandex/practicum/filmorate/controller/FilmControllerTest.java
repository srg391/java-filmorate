package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.filmexception.InvalidDescriptionException;
import ru.yandex.practicum.filmorate.exception.filmexception.InvalidDurationException;
import ru.yandex.practicum.filmorate.exception.filmexception.InvalidNameException;
import ru.yandex.practicum.filmorate.exception.filmexception.InvalidReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    @Test
    void validateFilmName() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, "", "about pirates", LocalDate.of(1986, 5, 8), 124);
        assertThrows(InvalidNameException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void validateFilmNameIsNull() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, null, "about pirates", LocalDate.of(1986, 5, 8), 124);
        assertThrows(InvalidNameException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void validateFilmDescription() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, "Pirates", "", LocalDate.of(1986, 5, 8), 124);
        assertThrows(InvalidDescriptionException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void validateFilmDescriptionIsNull() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, "Pirates", null, LocalDate.of(1986, 5, 8), 124);
        assertThrows(InvalidDescriptionException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void validateFilmDescriptionIsOverSymbols() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, "Pirates", "200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200200", LocalDate.of(1986, 5, 8), 124);
        assertThrows(InvalidDescriptionException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void validateFilmReleaseDate() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, "Pirates", "about pirates", LocalDate.of(1894, 5, 8), 124);
        assertThrows(InvalidReleaseDateException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void validateFilmReleaseDateIsNull() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, "Pirates", "about pirates", null, 124);
        assertThrows(InvalidReleaseDateException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void validateFilmDuration() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, "Pirates", "about pirates", LocalDate.of(1986, 5, 8), -124);
        assertThrows(InvalidDurationException.class, () -> filmController.validateFilm(film));
    }

    @Test
    void validateFilmDurationIsNull() {
        FilmController filmController = new FilmController();
        Film film = new Film(1L, "Pirates", "about pirates", LocalDate.of(1986, 5, 8), null);
        assertThrows(InvalidDurationException.class, () -> filmController.validateFilm(film));
    }

}