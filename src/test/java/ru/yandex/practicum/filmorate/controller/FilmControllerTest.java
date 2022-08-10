package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.filmexception.InvalidDescriptionException;
import ru.yandex.practicum.filmorate.exception.filmexception.InvalidReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {


    @Test
    void validateFilmReleaseDate() {
        UserService userService = new UserService();
        FilmService filmService = new FilmService();
        FilmController filmController = new FilmController(userService, filmService);
        Film film = new Film(1L, "Pirates", "about pirates", LocalDate.of(1894, 5, 8), 124,new HashSet<>());
        assertThrows(InvalidReleaseDateException.class, () -> filmController.validateFilm(film));
    }

}