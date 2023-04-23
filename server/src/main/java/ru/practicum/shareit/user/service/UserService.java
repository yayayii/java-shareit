package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    //create
    UserResponseDto addUser(UserRequestDto userDto);

    //read
    UserResponseDto getUser(Long userId);

    List<UserResponseDto> getAllUsers();

    //update
    UserResponseDto updateUser(Long userId, UserRequestDto userDto);

    //delete
    void deleteUser(Long userId);
}
