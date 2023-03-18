package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private int id;
    @NotNull @NotBlank @NotEmpty
    private String name;
    @NotNull @NotBlank @NotEmpty @Email
    private String email;

    public UserDto(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
