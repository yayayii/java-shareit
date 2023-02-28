package ru.practicum.shareit.item.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@AllArgsConstructor
@Slf4j
@Component
public class ItemStorageInMemory implements ItemStorage {
    private static int ID = 0;

    private final Map<Integer, Item> items = new HashMap<>();

    //create
    @Override
    public Item addItem(Item item, User owner) {
        item.setId(++ID);
        item.setOwner(owner);
        items.put(ID, item);
        log.info("Item " + ID + " was added.");
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

    @Override
    public Collection<Item> getAllItems(int ownerId) {
        List<Item> itemList = new ArrayList<>();
        for (Item item: items.values()) {
            if (item.getOwner().getId() == ownerId) {
                itemList.add(item);
            }
        }
        return itemList;
    }

    @Override
    public Collection<Item> getSearchedItems(String searchText) {
        List<Item> itemList = new ArrayList<>();
        for (Item item: items.values()) {
            if (item.getAvailable() &&
                    (item.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(searchText.toLowerCase()))) {
                itemList.add(item);
            }
        }
        return itemList;
    }

    //update
    @Override
    public Item updateItem(int itemId, Item item, User owner) {
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
        item.setOwner(owner);

        log.info("Item " + itemId + " was updated.");
        return otherItem;
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
