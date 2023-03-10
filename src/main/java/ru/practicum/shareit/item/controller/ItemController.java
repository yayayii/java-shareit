package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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
    ItemDto addItem(@Valid @RequestBody ItemDto itemDto,
                    @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.addItem(itemDto, ownerId);
    }

    //read
    @GetMapping("/{itemId}")
    ItemDto getItem(@PathVariable int itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    Collection<ItemDto> getAllItems(
            @RequestHeader(value = "X-Sharer-User-Id", required = false, defaultValue = "0") int ownerId) {
        return itemService.getAllItems(ownerId);
    }

    @GetMapping("/search")
    Collection<ItemDto> getSearchedItems(@RequestParam("text") String searchText) {
        return itemService.getSearchedItems(searchText);
    }

    //update
    @PatchMapping("/{itemId}")
    ItemDto updateItem(@PathVariable int itemId,
                    @RequestBody ItemDto itemDto,
                    @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.updateItem(itemId, itemDto, ownerId);
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
