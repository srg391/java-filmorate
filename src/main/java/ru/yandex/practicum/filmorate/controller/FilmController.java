package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.filmexception.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
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
    public Film createNewFilm(@Valid @RequestBody Film film) {
        if (film.getId() != null) {
            throw new InvalidIdFilmException("Передан фильм с непустым id!");
        } else if (films.containsKey(film.getId())) {
            throw new InvalidIdFilmException("Передан уже существующий фильм!");
        } else {
            film.setId(generateNewId());
            validateFilm(film);
            save(film);
            log.debug("Добавлен фильм :" + film);
            return film;
        }
    }
    void validateFilm(Film film) {
        if (film.getDescription().length()>200) {
            throw new InvalidDescriptionException("Описание не соотвествует!");
        }
        if (film.getReleaseDate().isBefore(RELEASE_MIN)) {
            throw new InvalidReleaseDateException("Дата релиза не соотвествует!");
        }
    }

    void save(Film film){
        films.put(film.getId(), film);
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            throw new InvalidIdFilmException("Id фильма не может быть пустым!");
        } else if (!films.containsKey(film.getId())) {
            throw new InvalidIdFilmException("Фильм не существует!");
        }
        validateFilm(film);
        save(film);
        log.debug("Изменен фильм :" + film);
        return film;
    }
}

