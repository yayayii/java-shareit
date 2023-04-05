package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

public interface ItemRequestService {
    ItemRequestResponseDto addItemRequest(ItemRequestRequestDto itemRequestDto, int requesterId);
}
