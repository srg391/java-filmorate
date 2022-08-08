package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.filmexception.InvalidIdFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Service
public class FilmService {
    @Autowired
    InMemoryUserStorage inMemoryUserStorage;
    @Autowired
    InMemoryFilmStorage inMemoryFilmStorage;

    public Film getFilm(long filmId) {
        final Film film = inMemoryFilmStorage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм c id=" + filmId + " не существует!");
        }
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
        if (!inMemoryFilmStorage.getFilmMap().containsKey(filmId)) {
            throw new NotFoundException("Фильм c id=" + filmId + " не существует!");
        }
        if (!inMemoryUserStorage.getUserMap().containsKey(userId)) {
            throw new NotFoundException("Пользователь c id=" + userId + " не существует!");
        }
        Film film = inMemoryFilmStorage.getFilm(filmId);
        if (film.getUserIds().contains(userId)) {
            deleteLike(userId, filmId);
        }
        User user = inMemoryUserStorage.getUser(userId);
        inMemoryFilmStorage.addLike(film, user);
    }

    public void deleteLike(long userId, long filmId) {
        if (!inMemoryFilmStorage.getFilmMap().containsKey(filmId)) {
            throw new NotFoundException("Фильм c id=" + filmId + " не существует!");
        }
        if (!inMemoryUserStorage.getUserMap().containsKey(userId)) {
            throw new NotFoundException("Пользователь c id=" + userId + "не существует!");
        }
        Film film = inMemoryFilmStorage.getFilm(filmId);
        User user = inMemoryUserStorage.getUser(userId);
        inMemoryFilmStorage.deleteLike(film, user);
    }


    public List<Film> getPopularFilms(Integer count) {
        Map<Film, Integer> popularFilms = new HashMap<>();
        popularFilms.clear();
        Map<Film, Integer> noPopularFilms = new HashMap<>();
        noPopularFilms.clear();
        for (long l : inMemoryFilmStorage.getFilmMap().keySet()) {
            Film film = inMemoryFilmStorage.getFilmMap().get(l);
            Set<Long> userIds = film.getUserIds();
            Integer likes = userIds.size();
            if (likes != 0) {
                popularFilms.put(film, likes);
            } else if (likes == 0) {
                noPopularFilms.put(film, likes);
            }
        }

        if (popularFilms.size() == 0) {
            List<Film> noPopularFilmsList = new ArrayList<>();
            for (Film film : noPopularFilms.keySet()) {
                noPopularFilmsList.add(film);
            }
            return noPopularFilmsList;
        }
        class CustomComparator<K, V extends Comparable> implements Comparator<K> {
            private Map<K, V> map;

            public CustomComparator(Map<K, V> map) {
                this.map = new HashMap<>(map);
            }

            @Override
            public int compare(K s1, K s2) {
                return map.get(s1).compareTo(map.get(s2)) * (-1);
            }
        }
        Comparator<Film> comparator = new CustomComparator(popularFilms);
        Map<Film, Integer> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(popularFilms);

        List<Film> popularFilmsList = new ArrayList<>();
        for (Film film : sortedMap.keySet()) {
            popularFilmsList.add(film);
        }
        if (count == null) {
            if (popularFilmsList.size() <= 10) {
                return popularFilmsList;
            }
        }
        List<Film> popularFilmsListSizeCount = new ArrayList<>();
        for (Film film : popularFilmsList) {
            if (popularFilmsListSizeCount.size() < count + 1) {
                popularFilmsListSizeCount.add(film);
            }
        }
        return popularFilmsListSizeCount;
    }
}
