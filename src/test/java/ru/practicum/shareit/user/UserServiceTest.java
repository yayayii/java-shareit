package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeAll;
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

    private static UserRequestDto testUserRequestDto;
    private static User[] testUsers;
    private static UserResponseDto[] testUserResponseDtos;

    @BeforeAll
    static void beforeAll() {
        testUserRequestDto = new UserRequestDto("test1", "test1");

        testUsers = new User[2];
        testUsers[0] = new User(1, "test1", "test1");
        testUsers[1] = new User(2, "test2", "test2");

        testUserResponseDtos = new UserResponseDto[2];
        testUserResponseDtos[0] = new UserResponseDto(1, "test1", "test1");
        testUserResponseDtos[1] = new UserResponseDto(2, "test2", "test2");
    }

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(mockUserRepository);
    }

    @Test
    void testAddUser() {
        when(mockUserRepository.save(any()))
                .thenReturn(testUsers[0]);
        assertDoesNotThrow(() -> userService.addUser(testUserRequestDto));
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
                .thenReturn(Optional.of(testUsers[0]));
        assertDoesNotThrow(() -> userService.getUser(1));
    }

    @Test
    void testGetAllUsers() {
        when(mockUserRepository.findAll())
                .thenReturn(List.of(testUsers[0], testUsers[1]));
        assertEquals(
                userService.getAllUsers(),
                List.of(testUserResponseDtos[0], testUserResponseDtos[1])
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
                .thenReturn(Optional.of(testUsers[0]));
        assertEquals(
                userService.updateUser(1, new UserRequestDto(null, null)),
                testUserResponseDtos[0]
        );
        assertEquals(
                userService.updateUser(1, new UserRequestDto("", "")),
                testUserResponseDtos[0]
        );
        assertEquals(
                userService.updateUser(1, new UserRequestDto("newTest", "newTest")),
                new UserResponseDto(1, "newTest", "newTest")
        );
        testUsers[0].setName("test1");
        testUsers[0].setEmail("test1");
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
