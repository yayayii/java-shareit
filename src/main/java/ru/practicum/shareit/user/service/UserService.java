package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.Collection;

public interface UserService {
    //create
    UserResponseDto addUser(UserRequestDto userDto);

    //read
    UserResponseDto getUser(int userId);

    Collection<UserResponseDto> getAllUsers();

    //update
    UserResponseDto updateUser(int userId, UserRequestDto userDto);

    //delete
    void deleteUser(int userId);

    void deleteAllUsers();
}
