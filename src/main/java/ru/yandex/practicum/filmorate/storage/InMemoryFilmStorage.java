package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Getter
@Setter
public class InMemoryFilmStorage implements FilmStorage {
    long generator = 0;
    HashMap<Long, Film> filmMap = new HashMap<>();

    public Film getFilm(long filmId) {
        return filmMap.get(filmId);
    }

    public List<Film> getAllFilms() {
        List<Film> filmsList = new ArrayList<>(filmMap.values());
        return filmsList;
    }

    public Long generateFilmId(Film film) {
        film.setId(++generator);
        return film.getId();
    }

    public Film saveFilm(Film film) {
        filmMap.put(film.getId(), film);
        return film;
    }

    public void addLike(Film film, User user) {
        film.getUserIds().add(user.getId());
    }

    public void deleteLike(Film film, User user) {
        film.getUserIds().remove(user.getId());
    }
}
