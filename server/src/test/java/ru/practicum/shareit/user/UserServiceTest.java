package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
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
import static org.mockito.ArgumentMatchers.anyLong;
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
        testUserRequestDto = new UserRequestDto("UserName1", "UserEmail1");

        testUsers = new User[2];
        testUsers[0] = new User(1L, "UserName1", "UserEmail1");
        testUsers[1] = new User(2L, "UserName2", "UserEmail2");

        testUserResponseDtos = new UserResponseDto[2];
        testUserResponseDtos[0] = new UserResponseDto(1L, "UserName1", "UserEmail1");
        testUserResponseDtos[1] = new UserResponseDto(2L, "UserName2", "UserEmail2");
    }


    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(mockUserRepository);
    }

    @Test
    void testAddUser() {
        when(mockUserRepository.save(any()))
                .thenReturn(testUsers[0]);
        Assertions.assertDoesNotThrow(() -> userService.addUser(testUserRequestDto));
    }

    @Test
    void testGetUser() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.getUser(1L)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUsers[0]));
        Assertions.assertDoesNotThrow(() -> userService.getUser(1L));
    }

    @Test
    void testGetAllUsers() {
        when(mockUserRepository.findAll())
                .thenReturn(List.of(testUsers[0], testUsers[1]));
        assertEquals(
                List.of(testUserResponseDtos[0], testUserResponseDtos[1]),
                userService.getAllUsers()
        );
    }

    @Test
    void testUpdateUser() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.updateUser(1L, null)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUsers[0]));
        assertEquals(
                testUserResponseDtos[0],
                userService.updateUser(1L, new UserRequestDto(null, null))
        );
        assertEquals(
                testUserResponseDtos[0],
                userService.updateUser(1L, new UserRequestDto("", ""))
        );
        assertEquals(
                new UserResponseDto(1L, "newTest", "newTest"),
                userService.updateUser(1L, new UserRequestDto("newTest", "newTest"))
        );
        testUsers[0].setName("test1");
        testUsers[0].setEmail("test1");
    }

    @Test
    void testDeleteUser() {
        when(mockUserRepository.existsById(anyLong()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.deleteUser(1L)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.existsById(anyLong()))
                .thenReturn(true);
        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }
}
