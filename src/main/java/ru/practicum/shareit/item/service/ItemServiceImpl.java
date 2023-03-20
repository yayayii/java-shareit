package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    //create
    @Override
    public ItemDto addItem(ItemDto itemDto, int ownerId) {
        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isEmpty()) {
            RuntimeException e = new NoSuchElementException("User with id = " + ownerId + " doesn't exist.");
            log.warn(e.getMessage());
            throw e;
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner.get());
        if (item.getAvailable() == null) {
            item.setAvailable(true);
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    //read
    @Override
    public ItemDto getItem(int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            RuntimeException e = new NoSuchElementException("Item with id = " + itemId + " doesn't exist.");
            log.warn(e.getMessage());
            throw e;
        }

        return ItemMapper.toItemDto(item.get());
    }

    @Override
    public Collection<ItemDto> getAllItems(int ownerId) {
        if (ownerId == 0) {
            return itemRepository.findAll()
                    .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        } else {
            if (!userRepository.existsById(ownerId)) {
                RuntimeException e = new NoSuchElementException("User with id = " + ownerId + " doesn't exist.");
                log.warn(e.getMessage());
                throw e;
            }

            return itemRepository.findByOwner_Id(ownerId)
                    .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        }
    }

    @Override
    public Collection<ItemDto> getSearchedItems(String searchText) {
        if (searchText.isEmpty() || searchText.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.getSearchedItems(searchText)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    //update
    @Override
    public ItemDto updateItem(int itemId, ItemDto itemDto, int ownerId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            RuntimeException e = new NoSuchElementException("Item with id = " + itemId + " doesn't exist.");
            log.warn(e.getMessage());
            throw e;
        }

        Item item = itemOptional.get();
        Item updatedItem = ItemMapper.toItem(itemDto);
        if (updatedItem.getName() != null) {
            item.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            item.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getAvailable() != null) {
            item.setAvailable(updatedItem.getAvailable());
        }
        if (item.getOwner() == null) {
            Optional<User> ownerOptional = userRepository.findById(ownerId);
            if (ownerOptional.isEmpty()) {
                RuntimeException e = new NoSuchElementException("User with id = " + ownerId + " doesn't exist.");
                log.warn(e.getMessage());
                throw e;
            }
            item.setOwner(ownerOptional.get());
        }
        item.setId(itemId);

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    //delete
    @Override
    public void deleteItem(int itemId) {
        if (!itemRepository.existsById(itemId)) {
            RuntimeException e = new NoSuchElementException("Item with id = " + itemId + " doesn't exist.");
            log.warn(e.getMessage());
            throw e;
        }

        itemRepository.deleteById(itemId);
    }

    @Override
    public void deleteAllItems() {
        itemRepository.deleteAll();
    }
}
