package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    //create
    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }
    //read
    @Override
    public User getUser(int userId) {
        return userStorage.getUser(userId);
    }
    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
    //update
    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }
    //delete
    @Override
    public void deleteUser(int userId) {
        userStorage.deleteUser(userId);
    }
    @Override
    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }
}
