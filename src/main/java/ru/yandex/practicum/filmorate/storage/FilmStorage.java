package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    public Optional<Film> getFilm(long filmId);

    public HashMap<Long, Film> getFilmMap();

    public List<Film> getAllFilms();

    public Long generateFilmId(Film film);

    public Film saveFilm(Film film);

    public void addLike(Film film, User user);

    public void deleteLike(Film film, User user);
}

