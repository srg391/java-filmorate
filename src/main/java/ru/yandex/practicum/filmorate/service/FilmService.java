package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.filmexception.InvalidIdFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class FilmService {
    @Autowired
    private UserStorage inMemoryUserStorage;
    @Autowired
    private FilmStorage inMemoryFilmStorage;

    public Film getFilm(long filmId) {
        final Film film = inMemoryFilmStorage.getFilm(filmId).orElseThrow(() -> new NotFoundException("Фильм c id=" + filmId + " не существует!"));
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> filmsList = inMemoryFilmStorage.getAllFilms();
        return filmsList;
    }

    public Film saveFilm(Film film) {
        return inMemoryFilmStorage.saveFilm(film);
    }

    public Film createFilm(Film film) {
        if (film.getId() != null) {
            throw new InvalidIdFilmException("Передан фильм с непустым id!");
        } else if (inMemoryFilmStorage.getFilmMap().containsKey(film.getId())) {
            throw new InvalidIdFilmException("Передан уже существующий фильм!");
        }
        inMemoryFilmStorage.generateFilmId(film);
        saveFilm(film);
        return film;
    }

    public Film putFilm(Film film) {
        if (film.getId() == null) {
            throw new InvalidIdFilmException("Id фильма не может быть пустым!");
        } else if (!inMemoryFilmStorage.getFilmMap().containsKey(film.getId())) {
            throw new NotFoundException("Фильм c id=" + film.getId() + " не существует!");
        }
        saveFilm(film);
        return film;
    }

    public void addLike(long userId, long filmId) {
        Film film = inMemoryFilmStorage.getFilm(filmId).orElseThrow(() -> new NotFoundException("Фильм c id=" + filmId + " не существует!"));
        if (film.getUserIds().contains(userId)) {
            deleteLike(userId, filmId);
        }
        User user = inMemoryUserStorage.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        inMemoryFilmStorage.addLike(film, user);
    }

    public void deleteLike(long userId, long filmId) {
        Film film = inMemoryFilmStorage.getFilm(filmId).orElseThrow(() -> new NotFoundException("Фильм c id=" + filmId + " не существует!"));
        User user = inMemoryUserStorage.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        inMemoryFilmStorage.deleteLike(film, user);
    }


    public List<Film> getPopularFilms(Integer count) {
        int countPopularFilms = 0;
        if (count == null) {
            countPopularFilms = 10;
            } else {
            countPopularFilms = count;
        }

        List<Film> popularFilmList = new ArrayList<>(inMemoryFilmStorage.getFilmMap().values());
        return (popularFilmList.stream()
                .sorted(new FilmComparator())
                .limit(countPopularFilms)
                .collect(Collectors.toList()));
    }
}
