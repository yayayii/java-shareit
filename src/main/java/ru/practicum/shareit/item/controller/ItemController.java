package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

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
    public List<ItemFullResponseDto> getAllItems(
            @RequestHeader("X-Sharer-User-Id") int ownerId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "999") int size
    ) {
        return itemService.getAllItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> getSearchedItems(
            @RequestParam("text") String searchText,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "999") int size
    ) {
        return itemService.getSearchedItems(searchText, from, size);
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
