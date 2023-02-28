package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
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
    User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    //read
    @GetMapping("/{userId}")
    User getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    //update
    @PatchMapping("/{userId}")
    User updateUser(@PathVariable int userId,
                    @RequestBody User user) {
        return userService.updateUser(userId, user);
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
