package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

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

    //create
    @Transactional
    @Override
    public ItemResponseDto addItem(ItemRequestDto itemDto, int ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NoSuchElementException("User id = " + ownerId + " doesn't exist."));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public CommentResponseDto addComment(CommentRequestDto commentDto, int itemId, int bookerId) {
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
    public ItemResponseDto getItem(int itemId, int userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item id = " + itemId + " doesn't exist."));
        ItemResponseDto itemDto = ItemMapper.toItemDto(item);
        if (userId == item.getOwner().getId()) {
            Booking lastBooking = bookingRepository.findLastBookingByItemId(itemDto.getId());
            Booking nextBooking = bookingRepository.findNextBookingByItemId(itemDto.getId());
            if (lastBooking == null) {
                itemDto.setLastBooking(null);
            } else {
                itemDto.setLastBooking(new BookingShortResponseDto(lastBooking.getId(), lastBooking.getBooker().getId()));
            }
            if (nextBooking == null) {
                itemDto.setNextBooking(null);
            } else {
                itemDto.setNextBooking(new BookingShortResponseDto(nextBooking.getId(), nextBooking.getBooker().getId()));
            }
        }
        itemDto.setComments(commentRepository.findCommentsByItem_Id(itemId)
                .stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));

        return itemDto;
    }

    @Override
    public Collection<ItemResponseDto> getAllItems(int ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NoSuchElementException("User id = " + ownerId + " doesn't exist.");
        }

        List<ItemResponseDto> items = itemRepository.findByOwner_IdOrderById(ownerId)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        Map<Integer, List<Booking>> lastBookings = bookingRepository.findLastBookings().stream()
                .collect(Collectors.groupingBy((booking) -> booking.getItem().getId()));
        Map<Integer, List<Booking>> nextBookings = bookingRepository.findNextBookings().stream()
                .collect(Collectors.groupingBy((booking) -> booking.getItem().getId()));
        Map<Integer, List<Comment>> comments = commentRepository.findAll().stream()
                .collect(Collectors.groupingBy((comment) -> comment.getItem().getId()));
        for (ItemResponseDto itemDto : items) {
            if (lastBookings.containsKey(itemDto.getId())) {
                Booking lastBooking = lastBookings.get(itemDto.getId()).get(0);
                itemDto.setLastBooking(new BookingShortResponseDto(lastBooking.getId(), lastBooking.getBooker().getId()));
            } else {
                itemDto.setLastBooking(null);
            }
            if (nextBookings.containsKey(itemDto.getId())) {
                Booking nextBooking = nextBookings.get(itemDto.getId()).get(0);
                itemDto.setNextBooking(new BookingShortResponseDto(nextBooking.getId(), nextBooking.getBooker().getId()));
            } else {
                itemDto.setNextBooking(null);
            }
            itemDto.setComments(comments.getOrDefault(itemDto.getId(), Collections.emptyList())
                    .stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        }

        return items;
    }

    @Override
    public Collection<ItemResponseDto> getSearchedItems(String searchText) {
        if (searchText.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(searchText, searchText)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    //update
    @Transactional
    @Override
    public ItemResponseDto updateItem(int itemId, ItemRequestDto itemDto, int ownerId) {
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
    public void deleteItem(int itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NoSuchElementException("Item id = " + itemId + " doesn't exist.");
        }

        itemRepository.deleteById(itemId);
    }

    @Transactional
    @Override
    public void deleteAllItems() {
        itemRepository.deleteAll();
    }
}
