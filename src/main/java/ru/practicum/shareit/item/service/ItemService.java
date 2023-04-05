package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    //create
    ItemResponseDto addItem(ItemRequestDto itemDto, int ownerId);

    CommentResponseDto addComment(CommentRequestDto commentDto, int itemId, int userId);

    //read
    ItemFullResponseDto getItem(int itemId, int userId);

    List<ItemFullResponseDto> getAllItems(int ownerId, int from, int size);

    List<ItemResponseDto> getSearchedItems(String searchText, int from, int size);

    //update
    ItemResponseDto updateItem(int itemId, ItemRequestDto itemDto, int ownerId);

    //delete
    void deleteItem(int itemId);

    void deleteAllItems();
}
