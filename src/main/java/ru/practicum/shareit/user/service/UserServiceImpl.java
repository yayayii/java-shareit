package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    //create
    @Transactional
    @Override
    public UserResponseDto addUser(UserRequestDto userDto) {
        User user = UserMapper.toUser(userDto);

        return UserMapper.toUserDto(userRepository.save(user));
    }

    //read
    @Override
    public UserResponseDto getUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User id = " + userId + " doesn't exist."));

        return UserMapper.toUserDto(user);
    }

    @Override
    public Collection<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    //update
    @Transactional
    @Override
    public UserResponseDto updateUser(int userId, UserRequestDto userDto) {
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
    public void deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User id = " + userId + " doesn't exist.");
        }

        userRepository.deleteById(userId);
    }

    @Transactional
    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
