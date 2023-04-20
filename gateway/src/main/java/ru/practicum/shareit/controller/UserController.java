package ru.practicum.shareit.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.dto.UserRequestDto;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;


@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    //create
    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Validated(Create.class) UserRequestDto userDto) {
        log.info("gateway UserController - addUser");
        ResponseEntity<Object> responseEntity = userClient.addUser(userDto);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    //read
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("gateway UserController - getUser");
        ResponseEntity<Object> responseEntity = userClient.getUser(userId);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("gateway UserController - getAllUsers");
        ResponseEntity<Object> responseEntity = userClient.getAllUsers();
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    //update
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable Long userId,
            @RequestBody @Validated(Update.class) UserRequestDto userDto
    ) {
        log.info("gateway UserController - updateUser");
        ResponseEntity<Object> responseEntity = userClient.updateUser(userId, userDto);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("gateway UserController - deleteUser");
        return new ResponseEntity<>(userClient.deleteUser(userId).getStatusCode());
    }
}
