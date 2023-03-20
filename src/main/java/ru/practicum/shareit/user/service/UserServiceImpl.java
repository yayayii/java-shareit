package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    //create
    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        return UserMapper.toUserDto(userRepository.save(user));
    }

    //read
    @Override
    public UserDto getUser(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            RuntimeException e = new NoSuchElementException("User with id = " + userId + " doesn't exist.");
            log.warn(e.getMessage());
            throw e;
        }

        return UserMapper.toUserDto(user.get());
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    //update
    @Override
    public UserDto updateUser(int userId, UserDto userDto) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            RuntimeException e = new NoSuchElementException("User with id = " + userId + " doesn't exist.");
            log.warn(e.getMessage());
            throw e;
        }

        User user = userOptional.get();
        User updatedUser = UserMapper.toUser(userDto);
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        user.setId(userId);

        return UserMapper.toUserDto(userRepository.save(user));
    }

    //delete
    @Override
    public void deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            RuntimeException e = new NoSuchElementException("User with id = " + userId + " doesn't exist.");
            log.warn(e.getMessage());
            throw e;
        }

        userRepository.deleteById(userId);
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
