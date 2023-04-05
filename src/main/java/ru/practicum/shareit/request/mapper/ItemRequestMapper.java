package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestFullResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequestFullResponseDto toFullItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestFullResponseDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemRequest.getItems().stream().map(ItemMapper::toItemDto).collect(Collectors.toList())
        );
    }

    public ItemRequestResponseDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestResponseDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public ItemRequest toItemRequest(ItemRequestRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getDescription()
        );
    }
}
