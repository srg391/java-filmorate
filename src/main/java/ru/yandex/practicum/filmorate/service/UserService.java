package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.userexception.InvalidIdUserException;
import ru.yandex.practicum.filmorate.exception.userexception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserStorage inMemoryUserStorage;

    public List<User> getAllUsers() {
        List<User> usersList = inMemoryUserStorage.getAllUsers();
        return usersList;
    }

    public User saveUser(User user) {
        return inMemoryUserStorage.saveUser(user);
    }

    public User getUser(long userId) {
        final User user = inMemoryUserStorage.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        return user;
    }

    public User createUser(User user) {
        List<String> users = new ArrayList<>();
        for (long l : inMemoryUserStorage.getUserMap().keySet()) {
            User userForSave = inMemoryUserStorage.getUserMap().get(l);
            String email = userForSave.getEmail();
            users.add(email);
        }
        if (users.contains(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован!");
        }
        inMemoryUserStorage.generateUserId(user);
        saveUser(user);
        return user;
    }

    public User putUser(User user) {
        if (user.getId() == null) {
            throw new InvalidIdUserException("Id пользователя не может быть пустым!");
        } else if (!inMemoryUserStorage.getUserMap().containsKey(user.getId())) {
            throw new NotFoundException("Пользователь c id=" + user.getId() + " не существует!");
        }
        saveUser(user);
        return user;
    }

    public void addFriend(long userId, long friendId) {
        User user = getUser(userId);
        if (user.getFriendIds().contains(friendId)) {
            deleteFriend(userId, friendId);
        }
        User friend = getUser(friendId);
        inMemoryUserStorage.addFriend(user, friend);
    }

    public void deleteFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        inMemoryUserStorage.deleteFriend(user, friend);
    }

    public List<User> getFriends(long userId) {
        final User user = getUser(userId);
        if (user.getFriendIds().size() == 0) {
            List<User> userFriendsEmpty = new ArrayList<>();
            return userFriendsEmpty;
        }
        List<User> userFriends = new ArrayList<>();
        for (long l : user.getFriendIds()) {
            userFriends.add(getUser(l));
        }
        return userFriends;
    }

    public List<User> getFriendsOtherUser(long userId, long otherId) {
        final User user = getUser(userId);
        List<Long> userFriends = new ArrayList<>(user.getFriendIds());
        final User other = getUser(otherId);
        List<Long> overFriends = new ArrayList<>(other.getFriendIds());

        return (overFriends.stream()
                .filter(userFriends::contains)
                .map(inMemoryUserStorage.getUserMap()::get)
                .collect(Collectors.toList()));
    }
}

