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
    Item addItem(@Valid @RequestBody Item item,
                 @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.addItem(item, ownerId);
    }

    //read
    @GetMapping("/{itemId}")
    Item getItem(@PathVariable int itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    Collection<Item> getAllItems(
            @RequestHeader(value = "X-Sharer-User-Id", required = false, defaultValue = "0") int ownerId) {
        return itemService.getAllItems(ownerId);
    }

    @GetMapping("/search")
    Collection<Item> getSearchedItems(@RequestParam("text") String searchText) {
        return itemService.getSearchedItems(searchText);
    }

    //update
    @PatchMapping("/{itemId}")
    Item updateItem(@PathVariable int itemId,
                    @RequestBody Item item,
                    @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.updateItem(itemId, item, ownerId);
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
