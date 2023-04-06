package ru.practicum.shareit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserShortResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

public class MapperTest {
    @Test
    void testUserMapper() {
        assertEquals(
                UserMapper.toUserDto(new User(1, "1", "1")),
                new UserResponseDto(1, "1", "1")
        );
        assertEquals(
                UserMapper.toShortUserDto(new User(1, "1", "1")),
                new UserShortResponseDto(1)
        );
        assertEquals(
                UserMapper.toUser(new UserRequestDto("1", "1")),
                new User("1", "1")
        );
    }
}
