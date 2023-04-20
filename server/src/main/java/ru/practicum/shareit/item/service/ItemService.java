package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.item.ItemRequestDto;
import ru.practicum.shareit.item.dto.item.ItemFullResponseDto;
import ru.practicum.shareit.item.dto.item.ItemResponseDto;

import java.util.List;

public interface ItemService {
    //create
    ItemResponseDto addItem(ItemRequestDto itemDto, Long ownerId);

    CommentResponseDto addComment(CommentRequestDto commentDto, Long itemId, Long bookerId);

    //read
    ItemFullResponseDto getItem(Long itemId, Long userId);

    List<ItemFullResponseDto> getAllItems(Long ownerId, int from, int size);

    List<ItemResponseDto> getSearchedItems(String searchText, int from, int size);

    //update
    ItemResponseDto updateItem(Long itemId, ItemRequestDto itemDto, Long ownerId);

    //delete
    void deleteItem(Long itemId);
}
