package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    //create
    User addUser(User user);
    //read
    User getUser(int userId);
    Collection<User> getAllUsers();
    //update
    User updateUser(int userId, User user);
    //delete
    void deleteUser(int userId);
    void deleteAllUsers();
}
