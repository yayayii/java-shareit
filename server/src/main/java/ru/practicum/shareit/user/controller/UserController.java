package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    //create
    @PostMapping
    public ResponseEntity<UserResponseDto> addUser(@RequestBody UserRequestDto userDto) {
        log.info("server UserController - addUser");
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    //read
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        log.info("server UserController - getUser");
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("server UserController - getAllUsers");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //update
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long userId,
            @RequestBody UserRequestDto userDto
    ) {
        log.info("server UserController - updateUser");
        return ResponseEntity.ok(userService.updateUser(userId, userDto));
    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("server UserController - deleteUser");
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
