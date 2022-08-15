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
    private UserStorage UserStorage;

    public List<User> getAllUsers() {
        List<User> usersList = UserStorage.getAllUsers();
        return usersList;
    }

    public User saveUser(User user) {
        return UserStorage.saveUser(user);
    }

    public User getUser(long userId) {
        final User user = UserStorage.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        return user;
    }

    public User createUser(User user) {
        UserStorage.saveUser(user);
        return user;
    }

    public User putUser(User user) {
        UserStorage.update(user);
        return user;
    }

    public User addFriend(long userId, long friendId) {
        User user = getUser(userId);
        getUser(friendId);
        user.getFriendIds().add(friendId);
        return UserStorage.update(user);
    }

    public User deleteFriend(long userId, long friendId) {
        User user = getUser(userId);
        getUser(friendId);
        user.getFriendIds().remove(friendId);
        return UserStorage.update(user);
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
                .flatMap(fId -> UserStorage.getUser(fId).stream())
                .collect(Collectors.toList()));
    }
}

