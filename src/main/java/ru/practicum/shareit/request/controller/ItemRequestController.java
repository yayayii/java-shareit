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

    @GetMapping
    public List<ItemRequestFullResponseDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        return itemRequestService.getItemRequests(requesterId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestFullResponseDto getItemRequest(@PathVariable int requestId) {
        return itemRequestService.getItemRequest(requestId);
    }
}
