package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRequestDto {
    @NotBlank(groups = Create.class)
    private final String name;
    @NotBlank(groups = Create.class) @Email(groups = {Create.class, Update.class})
    private final String email;
}
