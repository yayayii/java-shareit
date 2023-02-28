package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserStorageInMemory implements UserStorage {
    private int id;
    private final ItemStorage itemStorage;
    private final Map<Integer, User> users = new HashMap<>();

    //create
    @Override
    public User addUser(User user) {
        user.setId(++id);
        users.put(id, user);
        log.info("User " + id + " was added.");
        return user;
    }

    //read
    @Override
    public User getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        return users;
    }

    //update
    @Override
    public User updateUser(int userId, User user) {
        User otherUser = users.get(userId);
        if (user.getName() != null) {
            otherUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            otherUser.setEmail(user.getEmail());
        }
        log.info("User " + userId + " was updated.");
        return otherUser;
    }

    //delete
    @Override
    public void deleteUser(int userId) {
        for (Item item: itemStorage.getAllItems().values()) {
            if (item.getOwner().getId() == userId) {
                itemStorage.deleteItem(item.getId());
            }
        }
        users.remove(userId);
        log.info("User " + userId + " was deleted.");
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
        log.info("User storage was cleared");
    }
}
