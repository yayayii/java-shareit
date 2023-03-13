package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    //create
    UserDto addUser(UserDto userDto);

    //read
    UserDto getUser(int userId);

    Collection<UserDto> getAllUsers();

    //update
    UserDto updateUser(int userId, UserDto userDto);

    //delete
    void deleteUser(int userId);

    void deleteAllUsers();
}
