package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.filmexception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    static private final LocalDate RELEASE_MIN = LocalDate.of(1895, 12, 28);
    private Integer count;

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmsList = filmService.getAllFilms();
        log.debug("Количество постов :" + filmsList.size());
        return filmsList;
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable long filmId) {
        return filmService.getFilm(filmId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        Film savedFilm = filmService.saveFilm(film);
        log.debug("Добавлен фильм :" + savedFilm);
        return savedFilm;
    }

    void validateFilm(Film film) {
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
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10")  @Positive Integer count) {
        Integer countValid = count;
        return filmService.getPopularFilms(countValid);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable long userId, @PathVariable long filmId) {
        return filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable long userId, @PathVariable long filmId) {
        return filmService.deleteLike(userId, filmId);
    }
}

