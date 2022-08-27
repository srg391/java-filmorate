package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    public Optional<User> getUser(long userId);


    public List<User> getAllUsers();


    public User saveUser(User user);


    public User update(User user);

    public void clear();
}
