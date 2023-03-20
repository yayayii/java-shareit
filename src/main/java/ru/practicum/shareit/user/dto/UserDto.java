package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private final int id;
    @NotNull @NotBlank @NotEmpty
    private final String name;
    @NotNull @NotBlank @NotEmpty @Email
    private final String email;
}
