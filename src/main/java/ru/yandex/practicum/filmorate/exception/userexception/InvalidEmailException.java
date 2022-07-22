package ru.yandex.practicum.filmorate.exception.userexception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String s) {
        super(s);
    }
}

