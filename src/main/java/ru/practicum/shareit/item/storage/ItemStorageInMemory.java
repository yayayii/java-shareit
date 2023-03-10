package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ItemStorageInMemory implements ItemStorage {
    private int id;
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

    @Override
    public Collection<Item> getAllItems(int ownerId, Set<Integer> itemIds) {
        return itemIds.stream().map(this::getItem).collect(Collectors.toList());
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
    public Item updateItem(Item item) {
        Item otherItem = items.get(item.getId());
        if (item.getName() != null) {
            otherItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            otherItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            otherItem.setAvailable(item.getAvailable());
        }

        log.info("Item " + item.getId() + " was updated.");
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
