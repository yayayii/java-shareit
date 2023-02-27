package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    //create
    User addUser(User user);
    //read
    User getUser(int userId);
    Collection<User> getAllUsers();
    //update
    User updateUser(User user);
    //delete
    void deleteUser(int userId);
    void deleteAllUsers();
}
