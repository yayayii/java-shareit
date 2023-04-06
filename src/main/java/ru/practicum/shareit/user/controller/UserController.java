package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    //create
    @PostMapping
    public UserResponseDto addUser(@Validated(Create.class) @RequestBody UserRequestDto userDto) {
        return userService.addUser(userDto);
    }

    //read
    @GetMapping("/{userId}")
    public UserResponseDto getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    //update
    @PatchMapping("/{userId}")
    public UserResponseDto updateUser(
            @PathVariable int userId,
            @Validated(Update.class) @RequestBody UserRequestDto userDto
    ) {
        return userService.updateUser(userId, userDto);
    }

    //delete
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }
}
