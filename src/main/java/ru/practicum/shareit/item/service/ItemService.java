package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Collection;

public interface ItemService {
    //create
    ItemResponseDto addItem(ItemRequestDto itemDto, int ownerId);

    CommentDto addComment(CommentDto commentDto, int itemId, int userId);

    //read
    ItemResponseDto getItem(int itemId, int userId);

    Collection<ItemResponseDto> getAllItems(int ownerId);

    Collection<ItemResponseDto> getSearchedItems(String searchText);

    //update
    ItemResponseDto updateItem(int itemId, ItemRequestDto itemDto, int ownerId);

    //delete
    void deleteItem(int itemId);

    void deleteAllItems();
}
