package ru.practicum.shareit.user.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Slf4j
@Component
public class UserValidator {
    private final UserStorage userStorage;

    public void validateNewUser(User user) {
        for (User otherUser: userStorage.getAllUsers().values()) {
            validateName(user, otherUser);
            validateEmail(user, otherUser);
        }
    }

    public void validateUpdatedUser(int userId, User user) {
        for (User otherUser: userStorage.getAllUsers().values()) {
            if (otherUser.getId() != userId) {
                if (user.getName() != null) {
                    validateName(user, otherUser);
                }
                if (user.getEmail() != null) {
                    validateEmail(user, otherUser);
                }
            }
        }
    }

    public void validateId(int userId) {
        if (!userStorage.getAllUsers().containsKey(userId)) {
            RuntimeException exception = new NoSuchElementException("User with id = " + userId + " doesn't exist.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    private void validateName(User user, User otherUser) {
        if (user.getName().equals(otherUser.getName())) {
            RuntimeException exception = new ValidationException("User with name " + user.getName() + " already exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    private void validateEmail(User user, User otherUser) {
        if (user.getEmail().equals(otherUser.getEmail())) {
            RuntimeException exception = new ValidationException("User with email " + user.getEmail() + " already exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }
}
