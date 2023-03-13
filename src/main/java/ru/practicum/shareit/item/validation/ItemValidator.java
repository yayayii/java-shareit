package ru.practicum.shareit.item.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Slf4j
@Component
public class ItemValidator {
    private final ItemStorage itemStorage;

    public void validateUpdatedItem(int itemId, int ownerId) {
        if (itemStorage.getItem(itemId) == null) {
            RuntimeException exception = new NoSuchElementException("Item with id = " + itemId + " doesn't exist.");
            log.warn(exception.getMessage());
            throw exception;
        }
        if (ownerId != itemStorage.getItem(itemId).getOwner().getId()) {
            RuntimeException exception = new ForbiddenActionException("Changing owner is forbidden.");
            log.warn(exception.getMessage());
            throw exception;
        }
    }
}
