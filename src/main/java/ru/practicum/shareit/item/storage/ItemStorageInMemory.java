package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ItemStorageInMemory implements ItemStorage {
    private static int id = 0;
    private final Map<Integer, Item> items = new HashMap<>();

    //create
    @Override
    public Item addItem(Item item) {
        return null;
    }
    //read
    @Override
    public Item getItem(int itemId) {
        return null;
    }
    @Override
    public Map<Integer, Item> getAllItems() {
        return null;
    }
    //update
    @Override
    public Item updateItem(int itemId, Item item) {
        return null;
    }
    //delete
    @Override
    public void deleteItem(int itemId) {

    }
    @Override
    public void deleteAllItems() {

    }
}
