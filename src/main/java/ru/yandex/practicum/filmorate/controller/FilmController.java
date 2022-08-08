package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.filmexception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    @Autowired
    UserService userService;
    @Autowired
    FilmService filmService;
    private final LocalDate RELEASE_MIN = LocalDate.of(1895, 12, 28);
    private Integer count;

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmsList = filmService.getAllFilms();
        log.debug("Количество постов :" + filmsList.size());
        return filmsList;
    }

    @GetMapping("/{filmId}")
    Film getFilm(@PathVariable long filmId) {
        return filmService.getFilm(filmId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        Film savedFilm = filmService.createFilm(film);
        log.debug("Добавлен фильм :" + savedFilm);
        return savedFilm;
    }

    void validateFilm(Film film) {
        if (film.getDescription().length() > 200) {
            throw new InvalidDescriptionException("Описание не соотвествует!");
        }
        if (film.getReleaseDate().isBefore(RELEASE_MIN)) {
            throw new InvalidReleaseDateException("Дата релиза не соотвествует!");
        }
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        Film savedFilm = filmService.putFilm(film);
        log.debug("Изменен фильм :" + savedFilm);
        return savedFilm;
    }

    @GetMapping(value = "/popular")
    @ResponseBody
    public List<Film> getPopularFilms(@RequestParam(value = "count", required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long userId, @PathVariable long filmId) {
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long userId, @PathVariable long filmId) {
        filmService.deleteLike(userId, filmId);
    }
}

