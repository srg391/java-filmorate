package ru.yandex.practicum.filmorate.exception.filmexception;

public class InvalidNameException extends RuntimeException {
    public InvalidNameException(String s) {
        super(s);
    }
}

