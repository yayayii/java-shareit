package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    //create
    Item addItem(Item item, int ownerId);

    //read
    Item getItem(int itemId);

    Collection<Item> getAllItems(int ownerId);

    Collection<Item> getSearchedItems(String searchText);

    //update
    Item updateItem(int itemId, Item item, int ownerId);

    //delete
    void deleteItem(int itemId);

    void deleteAllItems();
}
