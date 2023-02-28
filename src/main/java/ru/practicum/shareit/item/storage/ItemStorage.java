package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Map;

public interface ItemStorage {
    //create
    Item addItem(Item item);
    //read
    Item getItem(int itemId);
    Map<Integer, Item> getAllItems();
    //update
    Item updateItem(int itemId, Item item);
    //delete
    void deleteItem(int itemId);
    void deleteAllItems();
}
