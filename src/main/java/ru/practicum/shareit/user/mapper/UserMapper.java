package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserShortResponseDto;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserMapper {
    public UserResponseDto toUserDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public UserShortResponseDto toShortUserDto(User user) {
        return new UserShortResponseDto(user.getId());
    }

    public User toUser(UserRequestDto userDto) {
        return new User(
                userDto.getName(),
                userDto.getEmail()
        );
    }
}
