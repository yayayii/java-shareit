package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.validation.ItemValidator;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@AllArgsConstructor
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemValidator itemValidator;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    //create
    @Override
    public Item addItem(Item item, int ownerId) {
        itemValidator.validateNewItem(item, ownerId);
        return itemStorage.addItem(item, userStorage.getUser(ownerId));
    }
    //read
    @Override
    public Item getItem(int itemId) {
        itemValidator.validateId(itemId);
        return itemStorage.getItem(itemId);
    }
    @Override
    public Collection<Item> getAllItems() {
        return itemStorage.getAllItems().values();
    }
    //update
    @Override
    public Item updateItem(int itemId, Item item, int ownerId) {
        if (item.getName() == null && item.getDescription() == null && item.getAvailable() == null) {
            RuntimeException exception = new NullPointerException("There is nothing to update.");
            log.warn(exception.getMessage());
            throw exception;
        }
        itemValidator.validateId(itemId);
        itemValidator.validateUpdatedItem(itemId, item, ownerId);
        return itemStorage.updateItem(itemId, item, userStorage.getUser(ownerId));
    }
    //delete
    @Override
    public void deleteItem(int itemId) {
        itemValidator.validateId(itemId);
        itemStorage.deleteItem(itemId);
    }
    @Override
    public void deleteAllItems() {
        itemStorage.deleteAllItems();
    }
}
