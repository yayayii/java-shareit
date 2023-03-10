package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    //create
    @PostMapping
    UserDto addUser(@Valid @RequestBody UserDto userDto) {
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
    UserDto updateUser(@PathVariable int userId,
                    @RequestBody UserDto userDto) {
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
