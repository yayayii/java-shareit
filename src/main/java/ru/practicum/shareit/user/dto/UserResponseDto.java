package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private final int id;
    private final String name;
    private final String email;
}
