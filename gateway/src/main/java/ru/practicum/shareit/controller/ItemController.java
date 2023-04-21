package ru.practicum.shareit.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.dto.request.CommentRequestDto;
import ru.practicum.shareit.dto.request.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@AllArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;


    //create
    @PostMapping
    public ResponseEntity<Object> addItem(
            @RequestBody @Valid ItemRequestDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        log.info("gateway ItemController - addItem");
        ResponseEntity<Object> responseEntity = itemClient.addItem(itemDto, ownerId);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestBody @Valid CommentRequestDto commentDto,
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long bookerId
    ) {
        log.info("gateway ItemController - addComment");
        ResponseEntity<Object> responseEntity = itemClient.addComment(commentDto, itemId, bookerId);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    //read
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(
            @PathVariable Long itemId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("gateway ItemController - getItem");
        ResponseEntity<Object> responseEntity = itemClient.getItem(itemId, userId);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "999") @Positive int size
    ) {
        log.info("gateway ItemController - getAllItems");
        ResponseEntity<Object> responseEntity = itemClient.getAllItems(ownerId, from, size);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearchedItems(
            @RequestParam("text") String searchText,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "999") @Positive int size
    ) {
        log.info("gateway ItemController - getSearchedItems");
        ResponseEntity<Object> responseEntity = itemClient.getSearchedItems(searchText, from, size);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    //update
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemRequestDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        log.info("gateway ItemController - updateItem");
        ResponseEntity<Object> responseEntity = itemClient.updateItem(itemId, itemDto, ownerId);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    //delete
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long itemId) {
        log.info("gateway ItemController - deleteItem");
        return new ResponseEntity<>(itemClient.deleteItem(itemId).getStatusCode());
    }
}
