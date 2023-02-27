package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserStorageInMemory implements UserStorage {
    private static int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    //create
    @Override
    public User addUser(User user) {
        user.setId(++id);
        users.put(id, user);
        return user;
    }
    //read
    @Override
    public User getUser(int userId) {
        return users.get(id);
    }
    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
    //update
    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }
    //delete
    @Override
    public void deleteUser(int userId) {
        users.remove(userId);
    }
    @Override
    public void deleteAllUsers() {
        id = 0;
        users.clear();
    }
}
