package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserValidator userValidator;
    private final UserStorage userStorage;

    //create
    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        userValidator.validateNewUser(user);
        return UserMapper.toUserDto(userStorage.addUser(user));
    }

    //read
    @Override
    public UserDto getUser(int userId) {
        if (userStorage.getUser(userId) == null) {
            RuntimeException exception = new NoSuchElementException("User with id = " + userId + " doesn't exist.");
            log.warn(exception.getMessage());
            throw exception;
        }
        return UserMapper.toUserDto(userStorage.getUser(userId));
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userStorage.getAllUsers().values()
                .stream().map(UserMapper::toUserDto).collect(Collectors.toCollection(TreeSet::new));
    }

    //update
    @Override
    public UserDto updateUser(int userId, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (user.getName() == null && user.getEmail() == null) {
            RuntimeException exception = new ValidationException("There is nothing to update.");
            log.warn(exception.getMessage());
            throw exception;
        }
        userValidator.validateUpdatedUser(userId, user);

        user.setId(userId);
        return UserMapper.toUserDto(userStorage.updateUser(user));
    }

    //delete
    @Override
    public void deleteUser(int userId) {
        if (userStorage.getUser(userId) == null) {
            RuntimeException exception = new NoSuchElementException("User with id = " + userId + " doesn't exist.");
            log.warn(exception.getMessage());
            throw exception;
        }
        userStorage.deleteUser(userId);
    }

    @Override
    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }
}
