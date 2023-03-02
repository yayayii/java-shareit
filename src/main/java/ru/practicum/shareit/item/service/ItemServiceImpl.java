package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.validation.ItemValidator;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemValidator itemValidator;
    private final UserValidator userValidator;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    //create
    @Override
    public ItemDto addItem(ItemDto itemDto, int ownerId) {
        Item item = ItemMapper.toItem(itemDto);

        userValidator.validateId(ownerId);

        item.setOwner(userStorage.getUser(ownerId));
        Item addedItem = itemStorage.addItem(item);
        userStorage.getUser(ownerId).getItemIds().add(addedItem.getId());
        return ItemMapper.toItemDto(addedItem);
    }

    //read
    @Override
    public ItemDto getItem(int itemId) {
        itemValidator.validateId(itemId);

        return ItemMapper.toItemDto(itemStorage.getItem(itemId));
    }

    @Override
    public Collection<ItemDto> getAllItems(int ownerId) {
        if (ownerId == 0) {
            return itemStorage.getAllItems().values()
                    .stream().map(ItemMapper::toItemDto).collect(Collectors.toCollection(TreeSet::new));
        } else {
            userValidator.validateId(ownerId);
            return itemStorage.getAllItems(ownerId, userStorage.getUser(ownerId).getItemIds())
                    .stream().map(ItemMapper::toItemDto).collect(Collectors.toCollection(TreeSet::new));
        }
    }

    @Override
    public Collection<ItemDto> getSearchedItems(String searchText) {
        if (searchText.isEmpty() || searchText.isBlank()) {
            return Collections.emptyList();
        }
        return itemStorage.getSearchedItems(searchText)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toCollection(TreeSet::new));
    }

    //update
    @Override
    public ItemDto updateItem(int itemId, ItemDto itemDto, int ownerId) {
        Item item = ItemMapper.toItem(itemDto);

        if (item.getName() == null && item.getDescription() == null && item.getAvailable() == null) {
            RuntimeException exception = new ValidationException("There is nothing to update.");
            log.warn(exception.getMessage());
            throw exception;
        }
        itemValidator.validateUpdatedItem(itemId, ownerId);

        item.setId(itemId);
        item.setOwner(userStorage.getUser(ownerId));
        return ItemMapper.toItemDto(itemStorage.updateItem(item));
    }

    //delete
    @Override
    public void deleteItem(int itemId) {
        itemValidator.validateId(itemId);
        itemStorage.getItem(itemId).getOwner().getItemIds().remove(itemId);
        itemStorage.deleteItem(itemId);
    }

    @Override
    public void deleteAllItems() {
        itemStorage.deleteAllItems();
    }
}
