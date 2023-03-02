package ru.practicum.shareit.user.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Slf4j
@Component
public class UserValidator {
    private final UserStorage userStorage;

    public void validateNewUser(User user) {
        for (User otherUser: userStorage.getAllUsers().values()) {
            validateName(user.getName(), otherUser.getName());
            validateEmail(user.getEmail(), otherUser.getEmail());
        }
    }

    public void validateUpdatedUser(int userId, User user) {
        for (Map.Entry<Integer, User> e : userStorage.getAllUsers().entrySet()) {
            if (e.getKey() != userId) {
                User otherUser = e.getValue();
                if (otherUser.getName() != null) {
                    validateName(otherUser.getName(), user.getName());
                }
                if (otherUser.getEmail() != null) {
                    validateName(otherUser.getEmail(), user.getEmail());
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

    private void validateName(String name, String otherName) {
        if (name.equals(otherName)) {
            RuntimeException exception = new ValidationException("User with name \"" + name + "\" already exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    private void validateEmail(String email, String otherEmail) {
        if (email.equals(otherEmail)) {
            RuntimeException exception = new ValidationException("User with email \"" + email + "\" already exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }
}
