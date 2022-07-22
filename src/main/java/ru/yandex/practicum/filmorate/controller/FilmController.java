package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.filmexception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private final LocalDate RELEASE_MIN = LocalDate.of(1895, 12, 28);
    private long idCounter = 0;

    public long generateNewId() {
        return ++idCounter;
    }
    @GetMapping
    public List<Film> findAllFilms() {
        List<Film> filmsList = new ArrayList<>(films.values());
        return filmsList;
    }

    @PostMapping
    public Film createNewFilm(@RequestBody Film film) {
        if (film.getId() != null) {
            throw new InvalidIdFilmException("Передан фильм с непустым id!");
        } else if (films.containsKey(film.getId())) {
            throw new InvalidIdFilmException("Передан уже существующий фильм!");
        } else {
            log.debug("Добавлен фильм :" + film);
            film.setId(generateNewId());
            validateFilm(film);
            save(film);
            return film;
        }
    }
    void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new InvalidNameException("Название не соотвествует!");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() || film.getDescription().length()>200) {
            throw new InvalidDescriptionException("Описание не соотвествует!");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(RELEASE_MIN)) {
            throw new InvalidReleaseDateException("Дата релиза не соотвествует!");
        }
        if (film.getDuration() == null || film.getDuration()<0) {
            throw new InvalidDurationException("Продолжительность не соотвествует!");
        }
    }

    void save(Film film){
        films.put(film.getId(), film);
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        if (film.getId() == null) {
            throw new InvalidIdFilmException("Id фильма не может быть пустым!");
        } else if (!films.containsKey(film.getId())) {
            throw new InvalidIdFilmException("Фильм не существует!");
        }
        log.debug("Изменен фильм :" + film);
        validateFilm(film);
        save(film);
        return film;
    }
}

