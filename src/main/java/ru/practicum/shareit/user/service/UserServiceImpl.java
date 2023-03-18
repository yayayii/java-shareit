package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
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
            throw new NoSuchElementException("User with id = " + userId + " doesn't exist.");
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
        if (userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id = " + userId + " doesn't exist.");
        }
        User user = userRepository.getReferenceById(userId);
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
        if (userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id = " + userId + " doesn't exist.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
