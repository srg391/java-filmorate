package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User getUser(long userId);

    public List<User> getAllUsers();


    public Long generateUserId(User user);

    public User saveUser(User user);

    public void addFriend(User user, User friend);

    public void deleteFriend(User user, User friend);
}
