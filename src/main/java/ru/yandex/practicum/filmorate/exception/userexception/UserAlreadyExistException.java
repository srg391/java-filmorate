package ru.yandex.practicum.filmorate.exception.userexception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String s) {
        super(s);
    }
}

