package ru.yandex.practicum.filmorate.exception.filmexception;

public class InvalidDurationException extends RuntimeException{
    public InvalidDurationException(String s) {
        super(s);
    }
}

