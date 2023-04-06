package ru.practicum.shareit.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository mockUserRepository;
    private UserService userService;
    private User testUser1 = new User(1, "1", "1");
    private User testUser2 = new User(2, "2", "2");

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(mockUserRepository);
    }

    @Test
    void testFindByIdException() {
        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        assertThrows(
                NoSuchElementException.class,
                () -> userService.getUser(1), "User id = 1 doesn't exist."
        );

        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.of(testUser1));
        assertDoesNotThrow(() -> userService.getUser(anyInt()));
    }

    @Test
    void testGetAllUsers() {
        when(mockUserRepository.findAll())
                .thenReturn(List.of(testUser1, testUser2));
        assertEquals(
                userService.getAllUsers(),
                List.of(UserMapper.toUserDto(testUser1), UserMapper.toUserDto(testUser2))
        );
    }

    @Test
    void testUpdateUser() {
        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.of(testUser1));

        assertEquals(
                userService.updateUser(anyInt(), new UserRequestDto(null, "1")),
                new UserResponseDto(1, "1", "1")
        );
        assertEquals(
                userService.updateUser(anyInt(), new UserRequestDto("1", null)),
                new UserResponseDto(1, "1", "1")
        );
        assertEquals(
                userService.updateUser(anyInt(), new UserRequestDto("", "1")),
                new UserResponseDto(1, "1", "1")
        );
        assertEquals(
                userService.updateUser(anyInt(), new UserRequestDto("1", "")),
                new UserResponseDto(1, "1", "1")
        );
        assertEquals(
                userService.updateUser(anyInt(), new UserRequestDto("2", "2")),
                new UserResponseDto(1, "2", "2")
        );
    }

    @Test
    void testExistsByIdException() {
        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(false);
        assertThrows(
                NoSuchElementException.class,
                () -> userService.deleteUser(1), "User id = 1 doesn't exist."
        );

        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(true);
        assertDoesNotThrow(() -> userService.deleteUser(anyInt()));
    }
}
