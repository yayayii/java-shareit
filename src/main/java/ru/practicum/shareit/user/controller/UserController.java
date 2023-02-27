package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    //create
    @PostMapping
    User addUser(@RequestBody User user) {
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
    @PatchMapping
    User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
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
