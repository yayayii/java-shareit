package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    //create
    @PostMapping
    UserDto addUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    //read
    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    Collection<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    //update
    @PatchMapping("/{userId}")
    UserDto updateUser(
            @PathVariable int userId,
            @Validated(Update.class) @RequestBody UserDto userDto
    ) {
        return userService.updateUser(userId, userDto);
    }

    //delete
    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
    }

    @DeleteMapping
    void deleteAllUsers() {
        userService.deleteAllUsers();
    }
}
