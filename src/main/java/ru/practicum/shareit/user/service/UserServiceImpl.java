package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.Collection;

@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserValidator userValidator;
    private final UserStorage userStorage;

    //create
    @Override
    public User addUser(User user) {
        userValidator.validateNewUser(user);
        return userStorage.addUser(user);
    }
    //read
    @Override
    public User getUser(int userId) {
        userValidator.validateId(userId);
        return userStorage.getUser(userId);
    }
    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers().values();
    }
    //update
    @Override
    public User updateUser(int userId, User user) {
        if (user.getName() == null && user.getEmail() == null) {
            RuntimeException exception = new NullPointerException("There is nothing to update.");
            log.warn(exception.getMessage());
            throw exception;
        }
        userValidator.validateId(userId);
        userValidator.validateUpdatedUser(userId, user);
        return userStorage.updateUser(userId, user);
    }
    //delete
    @Override
    public void deleteUser(int userId) {
        userValidator.validateId(userId);
        userStorage.deleteUser(userId);
    }
    @Override
    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }
}
