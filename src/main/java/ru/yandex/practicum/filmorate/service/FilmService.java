package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;


@Service
public class FilmService {
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private FilmStorage filmStorage;

    public Film getFilm(long filmId) {
        final Film film = filmStorage.getFilm(filmId).orElseThrow(() -> new NotFoundException("Фильм c id=" + filmId + " не существует!"));
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> filmsList = filmStorage.getAllFilms();
        return filmsList;
    }

    public Film saveFilm(Film film) {
        return filmStorage.saveFilm(film);
    }


    public Film putFilm(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(long userId, long filmId) {
        Film film = validateAndGetFilm(filmId, userId);
        film.getLikes().add(userId);
        return filmStorage.update(film);
    }

    public Film deleteLike(long userId, long filmId) {
        Film film = validateAndGetFilm(filmId, userId);
        film.getLikes().remove(userId);
        return filmStorage.update(film);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

    private Film validateAndGetFilm(long filmId, long userId) {
        userStorage.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + "не существует!"));

        return filmStorage.getFilm(filmId).orElseThrow(() -> new NotFoundException("Фильм с id" + filmId + "не существует!"));
    }
}
