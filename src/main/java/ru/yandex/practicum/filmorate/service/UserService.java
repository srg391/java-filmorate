package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.userexception.InvalidIdUserException;
import ru.yandex.practicum.filmorate.exception.userexception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class UserService {
    @Autowired
    InMemoryUserStorage inMemoryUserStorage;

    public List<User> getAllUsers() {
        List<User> usersList = inMemoryUserStorage.getAllUsers();
        return usersList;
    }

    public User saveUser(User user) {
        return inMemoryUserStorage.saveUser(user);
    }

    public User get(long userId) {
        final User user = inMemoryUserStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь c id=" + userId + " не существует!");
        }
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
        if (!inMemoryUserStorage.getUserMap().containsKey(userId)) {
            throw new NotFoundException("Пользователь c id=" + userId + " не существует!");
        }
        if (!inMemoryUserStorage.getUserMap().containsKey(friendId)) {
            throw new NotFoundException("Пользователь c id=" + userId + " не существует!");
        }
        User user = inMemoryUserStorage.getUser(userId);
        if (user.getFriendIds().contains(friendId)) {
            deleteFriend(userId, friendId);
        }
        User friend = inMemoryUserStorage.getUser(friendId);
        inMemoryUserStorage.addFriend(user, friend);
    }

    public void deleteFriend(long userId, long friendId) {
        if (!inMemoryUserStorage.getUserMap().containsKey(userId)) {
            throw new NotFoundException("Пользователь c id=" + userId + " не существует!");
        }
        if (!inMemoryUserStorage.getUserMap().containsKey(friendId)) {
            throw new NotFoundException("Пользователь c id=" + userId + " не существует!");
        }
        User user = inMemoryUserStorage.getUser(userId);
        User friend = inMemoryUserStorage.getUser(friendId);
        inMemoryUserStorage.deleteFriend(user, friend);
    }

    public List<User> getFriends(long userId) {
        final User user = inMemoryUserStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь c id=" + userId + " не существует!");
        }
        if (user.getFriendIds().size() == 0) {
            List<User> userFriendsEmpty = new ArrayList<>();
            return userFriendsEmpty;
        }
        List<User> userFriends = new ArrayList<>();
        for (long l : user.getFriendIds()) {
            userFriends.add(inMemoryUserStorage.getUser(l));
        }
        return userFriends;
    }

    public List<User> getFriendsOtherUser(long userId, long otherId) {
        final User user = inMemoryUserStorage.getUser(userId);

        if (user == null) {
            throw new NotFoundException("Пользователь c id=" + userId + " не существует!");
        }
        if (user.getFriendIds().size() == 0) {
            List<User> userFriendsEmpty = new ArrayList<>();
            return userFriendsEmpty;
        }

        final User other = inMemoryUserStorage.getUser(otherId);

        if (other == null) {
            throw new NotFoundException("Пользователь c id=" + userId + " не существует!");
        }
        if (other.getFriendIds().size() == 0) {
            List<User> userFriendsEmpty = new ArrayList<>();
            return userFriendsEmpty;
        }

        List<Long> userFriends = new ArrayList<>();
        for (long l : user.getFriendIds()) {
            userFriends.add(l);
        }
        Collections.sort(userFriends);

        List<Long> otherFriends = new ArrayList<>();
        for (long l : other.getFriendIds()) {
            otherFriends.add(l);
        }
        Collections.sort(otherFriends);

        List<User> jointFriends = new ArrayList<>();
        if (userFriends.size() > otherFriends.size()) {
            for (int i = 0; i < userFriends.size(); i++) {
                if (userFriends.contains(otherFriends.get(i))) {
                    jointFriends.add(inMemoryUserStorage.getUser(userFriends.get(i)));
                }
            }
        }
        if (otherFriends.size() >= userFriends.size()) {
            for (int i = 0; i < otherFriends.size(); i++) {
                if (otherFriends.contains(userFriends.get(i))) {
                    jointFriends.add(inMemoryUserStorage.getUser(otherFriends.get(i)));
                }
            }
        }
        return jointFriends;
    }
}

