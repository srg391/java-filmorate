package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Getter
@Setter
public class InMemoryFilmStorage implements FilmStorage {
    private long generator = 0;
    private HashMap<Long, Film> filmMap = new HashMap<>();

    public Optional<Film> getFilm(long filmId) {
        Optional<Film> filmOptional = Optional.ofNullable(filmMap.get(filmId));
        return filmOptional;
    }

    public List<Film> getAllFilms() {
        List<Film> filmsList = new ArrayList<>(filmMap.values());
        return filmsList;
    }

//    public Long generateFilmId(Film film) {
//        film.setId(++generator);
//        return film.getId();
//    }

    public Film saveFilm(Film film) {
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void clear() {

    }

//    public void addLike(Film film, User user) {
//        film.getUserIds().add(user.getId());
//    }

//    public void deleteLike(Film film, User user) {
//        film.getUserIds().remove(user.getId());
//    }
}
