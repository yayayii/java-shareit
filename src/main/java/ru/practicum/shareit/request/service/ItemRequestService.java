package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestFullResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto addItemRequest(ItemRequestRequestDto itemRequestDto, int requesterId);

    ItemRequestFullResponseDto getItemRequest(int userId, int requestId);

    List<ItemRequestFullResponseDto> getOwnItemRequests(int requesterId);

    List<ItemRequestFullResponseDto> getOtherItemRequests(int userId, int from, int size);
}
