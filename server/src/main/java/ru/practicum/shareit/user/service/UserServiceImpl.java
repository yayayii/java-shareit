package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    //create
    @Transactional
    @Override
    public UserResponseDto addUser(UserRequestDto userDto) {
        log.info("server UserService - addUser");

        User user = UserMapper.toUser(userDto);

        return UserMapper.toUserDto(userRepository.save(user));
    }

    //read
    @Override
    public UserResponseDto getUser(Long userId) {
        log.info("server UserService - getUser");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User id = " + userId + " doesn't exist."));

        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        log.info("server UserService - getAllUsers");

        return userRepository.findAll()
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    //update
    @Transactional
    @Override
    public UserResponseDto updateUser(Long userId, UserRequestDto userDto) {
        log.info("server UserService - updateUser");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User id = " + userId + " doesn't exist."));
        User updatedUser = UserMapper.toUser(userDto);
        if (updatedUser.getName() != null && !updatedUser.getName().isBlank()) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank()) {
            user.setEmail(updatedUser.getEmail());
        }

        return UserMapper.toUserDto(user);
    }

    //delete
    @Transactional
    @Override
    public void deleteUser(Long userId) {
        log.info("server UserService - deleteUser");

        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User id = " + userId + " doesn't exist.");
        }

        userRepository.deleteById(userId);
    }
}
