package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
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
    ItemResponseDto addItem(
            @Valid @RequestBody ItemRequestDto itemDto,
            @RequestHeader("X-Sharer-User-Id") int ownerId
    ) {
        return itemService.addItem(itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto addComment(
            @Valid @RequestBody CommentDto commentDto,
            @PathVariable int itemId,
            @RequestHeader(value = "X-Sharer-User-Id") int bookerId
    ) {
        return itemService.addComment(commentDto, itemId, bookerId);
    }

    //read
    @GetMapping("/{itemId}")
    ItemResponseDto getItem(
            @PathVariable int itemId,
            @RequestHeader(value = "X-Sharer-User-Id") int userId
    ) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    Collection<ItemResponseDto> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") int ownerId) {
        return itemService.getAllItems(ownerId);
    }

    @GetMapping("/search")
    Collection<ItemResponseDto> getSearchedItems(@RequestParam("text") String searchText) {
        return itemService.getSearchedItems(searchText);
    }

    //update
    @PatchMapping("/{itemId}")
    ItemResponseDto updateItem(
            @PathVariable int itemId,
            @RequestBody ItemRequestDto itemDto,
            @RequestHeader("X-Sharer-User-Id") int ownerId
    ) {
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
