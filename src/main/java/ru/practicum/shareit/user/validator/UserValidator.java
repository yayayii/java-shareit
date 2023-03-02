package ru.practicum.shareit.user.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Component
public class UserValidator {
    private final UserStorage userStorage;

    public void validateNewUser(User user) {
        validateName(user);
        validateEmail(user);
    }

    public void validateUpdatedUser(int userId, User user) {
        for (Integer otherUserId: userStorage.getAllUsers().keySet()) {
            if (otherUserId != userId) {
                if (user.getName() != null) {
                    validateName(user);
                }
                if (user.getEmail() != null) {
                    validateEmail(user);
                }
            }
        }
    }

    public void validateId(int userId) {
        if (userStorage.getUser(userId) == null) {
            RuntimeException exception = new NoSuchElementException("User with id = " + userId + " doesn't exist.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    private void validateName(User user) {
        if (userStorage.getAllUsers().values().
                stream().map(User::getName).collect(Collectors.toSet()).
                contains(user.getName())) {
            RuntimeException exception = new ValidationException("User with name \"" + user.getName() + "\" already exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    private void validateEmail(User user) {
        if (userStorage.getAllUsers().values().
                stream().map(User::getEmail).collect(Collectors.toSet()).
                contains(user.getEmail())) {
            RuntimeException exception = new ValidationException("User with email \"" + user.getEmail() + "\" already exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }
}
