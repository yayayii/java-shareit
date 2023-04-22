package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.item.ItemRequestDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.item.ItemFullResponseDto;
import ru.practicum.shareit.item.dto.item.ItemResponseDto;

import java.util.List;


@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    //create
    @PostMapping
    public ResponseEntity<ItemResponseDto> addItem(
            @RequestBody ItemRequestDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        log.info("server ItemController - addItem");
        return ResponseEntity.ok(itemService.addItem(itemDto, ownerId));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponseDto> addComment(
            @RequestBody CommentRequestDto commentDto,
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long bookerId
    ) {
        log.info("server ItemController - addComment");
        return ResponseEntity.ok(itemService.addComment(commentDto, itemId, bookerId));
    }

    //read
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemFullResponseDto> getItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("server ItemController - getItem");
        return ResponseEntity.ok(itemService.getItem(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemFullResponseDto>> getAllItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam int from,
            @RequestParam int size
    ) {
        log.info("server ItemController - getAllItems");
        return ResponseEntity.ok(itemService.getAllItems(ownerId, from, size));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseDto>> getSearchedItems(
            @RequestParam String searchText,
            @RequestParam int from,
            @RequestParam int size
    ) {
        log.info("server ItemController - getSearchedItems");
        return ResponseEntity.ok(itemService.getSearchedItems(searchText, from, size));
    }

    //update
    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemRequestDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        log.info("server ItemController - updateItem");
        return ResponseEntity.ok(itemService.updateItem(itemId, itemDto, ownerId));
    }

    //delete
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        log.info("server ItemController - deleteItem");
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }
}
