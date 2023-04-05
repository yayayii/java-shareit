package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
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
    public ItemResponseDto addItem(
            @Valid @RequestBody ItemRequestDto itemDto,
            @RequestHeader("X-Sharer-User-Id") int ownerId
    ) {
        return itemService.addItem(itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(
            @Valid @RequestBody CommentRequestDto commentDto,
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int bookerId
    ) {
        return itemService.addComment(commentDto, itemId, bookerId);
    }

    //read
    @GetMapping("/{itemId}")
    public ItemFullResponseDto getItem(
            @PathVariable int itemId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public Collection<ItemFullResponseDto> getAllItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.getAllItems(ownerId);
    }

    @GetMapping("/search")
    public Collection<ItemResponseDto> getSearchedItems(@RequestParam("text") String searchText) {
        return itemService.getSearchedItems(searchText);
    }

    //update
    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(
            @PathVariable int itemId,
            @RequestBody ItemRequestDto itemDto,
            @RequestHeader("X-Sharer-User-Id") int ownerId
    ) {
        return itemService.updateItem(itemId, itemDto, ownerId);
    }

    //delete
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable int itemId) {
        itemService.deleteItem(itemId);
    }

    @DeleteMapping
    public void deleteAllItems() {
        itemService.deleteAllItems();
    }
}
