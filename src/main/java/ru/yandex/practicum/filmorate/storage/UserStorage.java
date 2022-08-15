package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    public Optional<User> getUser(long userId);

//    public HashMap<Long, User> getUserMap();

    public List<User> getAllUsers();


//    public Long generateUserId(User user);

    public User saveUser(User user);

//    public void addFriend(User user, User friend);
//
//    public void deleteFriend(User user, User friend);

    public User update(User user);
    public void clear();
}
