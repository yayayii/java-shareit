package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.validation.ItemValidator;

import java.util.Collection;

@AllArgsConstructor
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemValidator itemValidator;
    private final ItemStorage itemStorage;

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
    public Collection<Item> getAllItems() {
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
