package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestFullResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto addItemRequest(
            @Valid @RequestBody ItemRequestRequestDto itemRequestDto,
            @RequestHeader("X-Sharer-User-Id") int requesterId
    ) {
        return itemRequestService.addItemRequest(itemRequestDto, requesterId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestFullResponseDto getItemRequest(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @PathVariable int requestId) {
        return itemRequestService.getItemRequest(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestFullResponseDto> getOwnItemRequests(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        return itemRequestService.getOwnItemRequests(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestFullResponseDto> getOtherItemRequests(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "999") int size
    ) {
        return itemRequestService.getOtherItemRequests(userId, from, size);
    }
}
