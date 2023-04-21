package ru.practicum.shareit.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemRequestClient;
import ru.practicum.shareit.dto.request.ItemRequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@AllArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;


    @PostMapping
    public ResponseEntity<Object> addItemRequest(
            @RequestBody @Valid ItemRequestRequestDto itemRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long requesterId
    ) {
        log.info("gateway ItemRequestController - addItemRequest");
        ResponseEntity<Object> responseEntity = itemRequestClient.addItemRequest(itemRequestDto, requesterId);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(
            @PathVariable Long requestId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("gateway ItemRequestController - getItemRequest");
        ResponseEntity<Object> responseEntity = itemRequestClient.getItemRequest(userId, requestId);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @GetMapping
    public ResponseEntity<Object> getOwnItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long requesterId
    ) {
        log.info("gateway ItemRequestController - getOwnItemRequests");
        ResponseEntity<Object> responseEntity = itemRequestClient.getOwnItemRequests(requesterId);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "999") @Positive int size
    ) {
        log.info("gateway ItemRequestController - getOtherItemRequests");
        ResponseEntity<Object> responseEntity = itemRequestClient.getOtherItemRequests(userId, from, size);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }
}
