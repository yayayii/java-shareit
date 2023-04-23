package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestFullResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestResponseDto> addItemRequest(
            @RequestBody ItemRequestRequestDto itemRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long requesterId
    ) {
        log.info("server ItemRequestController - addItemRequest");
        return ResponseEntity.ok(itemRequestService.addItemRequest(itemRequestDto, requesterId));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestFullResponseDto> getItemRequest(
            @PathVariable Long requestId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("server ItemRequestController - getItemRequest");
        return ResponseEntity.ok(itemRequestService.getItemRequest(userId, requestId));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestFullResponseDto>> getOwnItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long requesterId
    ) {
        log.info("server ItemRequestController - getOwnItemRequests");
        return ResponseEntity.ok(itemRequestService.getOwnItemRequests(requesterId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestFullResponseDto>> getOtherItemRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam int from,
            @RequestParam int size
    ) {
        log.info("server ItemRequestController - getOtherItemRequests");
        return ResponseEntity.ok(itemRequestService.getOtherItemRequests(userId, from, size));
    }
}
