package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Getter
@Setter
public class InMemoryUserStorage implements UserStorage{
    private long generator = 0;
    private HashMap<Long, User> userMap = new HashMap<>();

    public User getUser(long userId) {
        Optional<User> userOptional = Optional.ofNullable(userMap.get(userId));
        User user = userOptional.orElseThrow(() -> new NotFoundException("Пользователь c id=" + userId + " не существует!"));
        return user;
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>(userMap.values());
        return usersList;
    }

    public Long generateUserId(User user) {
        user.setId(++generator);
        return user.getId();
    }

    public User saveUser(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    public void addFriend(User user, User friend) {
        user.getFriendIds().add(friend.getId());
        friend.getFriendIds().add(user.getId());
    }

    public void deleteFriend(User user, User friend) {
        user.getFriendIds().remove(friend.getId());
        friend.getFriendIds().remove(user.getId());
    }
}
