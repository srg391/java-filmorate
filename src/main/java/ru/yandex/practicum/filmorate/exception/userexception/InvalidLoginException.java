package ru.yandex.practicum.filmorate.exception.userexception;

public class InvalidLoginException extends RuntimeException{
    public InvalidLoginException(String s) {
        super(s);
    }
}

