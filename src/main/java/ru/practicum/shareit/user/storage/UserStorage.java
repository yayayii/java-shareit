package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Map;

public interface UserStorage {
    //create
    User addUser(User user);

    //read
    User getUser(int userId);

    Map<Integer, User> getAllUsers();

    //update
    User updateUser(int userId, User user);

    //delete
    void deleteUser(int userId);

    void deleteAllUsers();
}
