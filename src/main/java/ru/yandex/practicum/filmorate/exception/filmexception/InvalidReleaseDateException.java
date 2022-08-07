package ru.yandex.practicum.filmorate.exception.filmexception;

public class InvalidReleaseDateException extends RuntimeException{
    public InvalidReleaseDateException(String s) {
        super(s);
    }
}

