package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    //create
    @PostMapping
    Item addItem(@Valid @RequestBody Item item) {
        return itemService.addItem(item);
    }
    //read
    @GetMapping("/{itemId}")
    Item getItem(@PathVariable int itemId) {
        return itemService.getItem(itemId);
    }
    @GetMapping
    Collection<Item> getAllItems() {
        return itemService.getAllItems();
    }
    //update
    @PatchMapping("/{itemId}")
    Item updateItem(@PathVariable int itemId,
                    @RequestBody Item item) {
        return itemService.updateItem(itemId, item);
    }
    //delete
    @DeleteMapping("/{itemId}")
    void deleteItem(@PathVariable int itemId) {
        itemService.deleteItem(itemId);
    }
    @DeleteMapping
    void deleteAllItems() {
        itemService.deleteAllItems();
    }
}
