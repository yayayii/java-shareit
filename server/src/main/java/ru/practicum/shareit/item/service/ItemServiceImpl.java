package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.item.ItemRequestDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.item.ItemFullResponseDto;
import ru.practicum.shareit.item.dto.item.ItemResponseDto;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    //create
    @Transactional
    @Override
    public ItemResponseDto addItem(ItemRequestDto itemDto, Long ownerId) {
        log.info("server ItemService - addItem");

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NoSuchElementException("User id = " + ownerId + " doesn't exist."));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NoSuchElementException("Item Request id = " + itemDto.getRequestId() + " doesn't exist."));
            item.setRequest(itemRequest);
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public CommentResponseDto addComment(CommentRequestDto commentDto, Long itemId, Long bookerId) {
        log.info("server ItemService - addComment");

        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NoSuchElementException("User id = " + bookerId + " doesn't exist."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item id = " + itemId + " doesn't exist."));
        if (item.getOwner().getId() == bookerId) {
            throw new NoSuchElementException("Owner of the item can't leave comments on his own items.");
        }
        if (bookingRepository.findLastBookingByItemIdAndBookerId(itemId, bookerId) == null) {
            throw new ValidationException("You must have this item in the past in order to leave a comment.");
        }

        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(booker);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    //read
    @Override
    public ItemFullResponseDto getItem(Long itemId, Long userId) {
        log.info("server ItemService - getItem");

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item id = " + itemId + " doesn't exist."));
        ItemFullResponseDto itemDto = ItemMapper.toFullItemDto(item);
        if (userId == item.getOwner().getId()) {
            setBookingsForItem(itemDto);
        }
        itemDto.setComments(commentRepository.findCommentsByItem_Id(itemId)
                .stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));

        return itemDto;
    }

    @Override
    public List<ItemFullResponseDto> getAllItems(Long ownerId, int from, int size) {
        log.info("server ItemService - getAllItems");

        if (!userRepository.existsById(ownerId)) {
            throw new NoSuchElementException("User id = " + ownerId + " doesn't exist.");
        }

        List<ItemFullResponseDto> items = itemRepository.findByOwner_IdOrderById(ownerId, PageRequest.of(from / size, size))
                .stream().map(ItemMapper::toFullItemDto).collect(Collectors.toList());
        for (ItemFullResponseDto itemDto : items) {
            setBookingsForItem(itemDto);
            itemDto.setComments(commentRepository.findCommentsByItem_Id(itemDto.getId())
                    .stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        }

        return items;
    }

    @Override
    public List<ItemResponseDto> getSearchedItems(String searchText, int from, int size) {
        log.info("server ItemService - getSearchedItems");

        if (searchText.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(searchText, searchText, PageRequest.of(from / size, size))
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    //update
    @Transactional
    @Override
    public ItemResponseDto updateItem(Long itemId, ItemRequestDto itemDto, Long ownerId) {
        log.info("server ItemService - updateItem");

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item id = " + itemId + " doesn't exist."));
        if (item.getOwner() == null) {
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new NoSuchElementException("User id = " + ownerId + " doesn't exist."));
            item.setOwner(owner);
        } else {
            if (item.getOwner().getId() != ownerId) {
                throw new ForbiddenActionException("Changing owner is forbidden.");
            }
        }
        Item updatedItem = ItemMapper.toItem(itemDto);
        if (updatedItem.getName() != null && !updatedItem.getName().isBlank()) {
            item.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null && !updatedItem.getDescription().isBlank()) {
            item.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getAvailable() != null) {
            item.setAvailable(updatedItem.getAvailable());
        }

        return ItemMapper.toItemDto(item);
    }

    //delete
    @Transactional
    @Override
    public void deleteItem(Long itemId) {
        log.info("server ItemService - deleteItem");

        if (!itemRepository.existsById(itemId)) {
            throw new NoSuchElementException("Item id = " + itemId + " doesn't exist.");
        }

        itemRepository.deleteById(itemId);
    }


    private void setBookingsForItem(ItemFullResponseDto itemDto) {
        Booking lastBooking = bookingRepository.findLastBookingByItemId(itemDto.getId());
        Booking nextBooking = bookingRepository.findNextBookingByItemId(itemDto.getId());
        itemDto.setLastBooking(lastBooking == null ? null
                : new BookingShortResponseDto(lastBooking.getId(), lastBooking.getBooker().getId()));
        itemDto.setNextBooking(nextBooking == null ? null
                : new BookingShortResponseDto(nextBooking.getId(), nextBooking.getBooker().getId()));
    }
}
