package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestFullResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto addItemRequest(ItemRequestRequestDto itemRequestDto, Long requesterId);

    ItemRequestFullResponseDto getItemRequest(Long requestId, Long userId);

    List<ItemRequestFullResponseDto> getOwnItemRequests(Long requesterId);

    List<ItemRequestFullResponseDto> getOtherItemRequests(Long userId, int from, int size);
}
