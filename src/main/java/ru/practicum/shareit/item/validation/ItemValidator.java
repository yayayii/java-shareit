package ru.practicum.shareit.item.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Slf4j
@Component
public class ItemValidator {
    private final ItemStorage itemStorage;

    public void validateNewItem(Item item) {
        for (Item otherItem: itemStorage.getAllItems().values()) {
            validateName(item, otherItem);
        }
    }

    public void validateUpdatedItem(int itemId, Item item) {
        for (Item otherItem: itemStorage.getAllItems().values()) {
            if (otherItem.getId() != itemId) {
                if (item.getName() != null) {
                    validateName(item, otherItem);
                }
            }
        }
    }

    public void validateId(int itemId) {
        if (!itemStorage.getAllItems().containsKey(itemId)) {
            RuntimeException exception = new NoSuchElementException("Item with id = " + itemId + " doesn't exist.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }

    private void validateName(Item item, Item otherItem) {
        if (item.getName().equals(otherItem.getName())) {
            RuntimeException exception = new ValidationException("Item with name " + item.getName() + " already exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }
}
