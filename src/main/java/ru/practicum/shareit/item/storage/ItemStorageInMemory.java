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
        item.setId(++id);
        items.put(id, item);
        log.info("Item " + id + " was added.");
        return item;
    }
    //read
    @Override
    public Item getItem(int itemId) {
        return items.get(itemId);
    }
    @Override
    public Map<Integer, Item> getAllItems() {
        return items;
    }
    //update
    @Override
    public Item updateItem(int itemId, Item item) {
        Item otherItem = items.get(itemId);
        if (item.getName() != null) {
            otherItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            otherItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            otherItem.setAvailable(item.getAvailable());
        }
        log.info("Item " + itemId + " was updated.");
        return null;
    }
    //delete
    @Override
    public void deleteItem(int itemId) {
        items.remove(itemId);
        log.info("Item " + itemId + " was deleted.");
    }
    @Override
    public void deleteAllItems() {
        items.clear();
        log.info("Item storage was cleared");
    }
}
