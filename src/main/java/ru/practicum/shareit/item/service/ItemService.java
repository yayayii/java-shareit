package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    //create
    ItemDto addItem(ItemDto itemDto, int ownerId);

    //read
    ItemDto getItem(int itemId, int userId);

    Collection<ItemDto> getAllItems(int ownerId);

    Collection<ItemDto> getSearchedItems(String searchText);

    //update
    ItemDto updateItem(int itemId, ItemDto itemDto, int ownerId);

    //delete
    void deleteItem(int itemId);

    void deleteAllItems();
}
