package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository mockUserRepository;
    private UserService userService;

    private UserRequestDto testUserRequestDto1 = new UserRequestDto("test1", "test1");
    private User testUser1 = new User(1, "test1", "test1");
    private User testUser2 = new User(2, "test2", "test2");
    private UserResponseDto testUserResponseDto1 = new UserResponseDto(1, "test1", "test1");
    private UserResponseDto testUserResponseDto2 = new UserResponseDto(2, "test2", "test2");

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(mockUserRepository);
    }

    @Test
    void testAddUser() {
        when(mockUserRepository.save(any()))
                .thenReturn(testUser1);
        assertDoesNotThrow(() -> userService.addUser(testUserRequestDto1));
    }

    @Test
    void testGetUser() {
        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.getUser(1)
        );
        assertEquals(
                exception.getMessage(),
                "User id = 1 doesn't exist."
        );

        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.of(testUser1));
        assertDoesNotThrow(() -> userService.getUser(1));
    }

    @Test
    void testGetAllUsers() {
        when(mockUserRepository.findAll())
                .thenReturn(List.of(testUser1, testUser2));
        assertEquals(
                userService.getAllUsers(),
                List.of(testUserResponseDto1, testUserResponseDto2)
        );
    }

    @Test
    void testUpdateUser() {
        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.updateUser(1, null)
        );
        assertEquals(
                exception.getMessage(),
                "User id = 1 doesn't exist."
        );

        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.of(testUser1));
        assertEquals(
                userService.updateUser(1, new UserRequestDto(null, null)),
                new UserResponseDto(1, "test1", "test1")
        );
        assertEquals(
                userService.updateUser(1, new UserRequestDto("", "")),
                new UserResponseDto(1, "test1", "test1")
        );
        assertEquals(
                userService.updateUser(1, new UserRequestDto("newTest", "newTest")),
                new UserResponseDto(1, "newTest", "newTest")
        );
        testUser1.setName("test1");
        testUser1.setEmail("test1");
    }

    @Test
    void testDeleteUser() {
        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.deleteUser(1)
        );
        assertEquals(
                exception.getMessage(),
                "User id = 1 doesn't exist."
        );

        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(true);
        assertDoesNotThrow(() -> userService.deleteUser(1));
    }
}
