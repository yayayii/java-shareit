package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    //create
    Item addItem(Item item);
    //read
    Item getItem(int itemId);
    Collection<Item> getAllItems();
    //update
    Item updateItem(int itemId, Item item);
    //delete
    void deleteItem(int itemId);
    void deleteAllItems();
}
