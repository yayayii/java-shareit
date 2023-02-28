package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Map;

public interface ItemStorage {
    //create
    Item addItem(Item item, User owner);
    //read
    Item getItem(int itemId);
    Map<Integer, Item> getAllItems();
    //update
    Item updateItem(int itemId, Item item, User owner);
    //delete
    void deleteItem(int itemId);
    void deleteAllItems();
}
