package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ItemStorage {
    //create
    Item addItem(Item item);

    //read
    Item getItem(int itemId);

    Map<Integer, Item> getAllItems();

    Collection<Item> getAllItems(int ownerId, Set<Integer> itemIds);

    Collection<Item> getSearchedItems(String searchText);

    //update
    Item updateItem(Item item);

    //delete
    void deleteItem(int itemId);

    void deleteAllItems();
}
