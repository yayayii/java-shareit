package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemFullResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class ItemMapper {
    public ItemFullResponseDto toFullItemDto(Item item) {
        if (item.getRequest() == null) {
            return new ItemFullResponseDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    null
            );
        } else {
            return new ItemFullResponseDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequest().getId()
            );
        }
    }

    public ItemResponseDto toItemDto(Item item) {
        if (item.getRequest() == null) {
            return new ItemResponseDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    null
            );
        } else {
            return new ItemResponseDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequest().getId()
            );
        }
    }

    public ItemShortResponseDto toShortItemDto(Item item) {
        return new ItemShortResponseDto(
                item.getId(),
                item.getName()
        );
    }

    public Item toItem(ItemRequestDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
    }
}
