package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    //create
    @Override
    public ItemDto addItem(ItemDto itemDto, int ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NoSuchElementException("User id = " + ownerId + " doesn't exist."));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        if (item.getAvailable() == null) {
            item.setAvailable(true);
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    //read
    @Override
    public ItemDto getItem(int itemId, int userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item id = " + itemId + " doesn't exist."));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (userId == item.getOwner().getId()) {
            Booking lastBooking = bookingRepository.findLastBookingByItemId(itemId);
            Booking nextBooking = bookingRepository.findNextBookingByItemId(itemId);
            if (lastBooking == null) {
                itemDto.setLastBooking(null);
            } else {
                itemDto.setLastBooking(new BookingShort(lastBooking.getId(), lastBooking.getBooker().getId()));
            }
            if (nextBooking == null) {
                itemDto.setNextBooking(null);
            } else {
                itemDto.setNextBooking(new BookingShort(nextBooking.getId(), nextBooking.getBooker().getId()));
            }
        }

        return itemDto;
    }

    @Override
    public Collection<ItemDto> getAllItems(int ownerId) {
        if (ownerId == 0) {
            return itemRepository.findAll()
                    .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        } else {
            if (!userRepository.existsById(ownerId)) {
                throw new NoSuchElementException("User id = " + ownerId + " doesn't exist.");
            }

            List<ItemDto> items = itemRepository.findByOwner_IdOrderById(ownerId)
                    .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());

            for (ItemDto i : items) {
                Booking lastBooking = bookingRepository.findLastBookingByItemId(i.getId());
                Booking nextBooking = bookingRepository.findNextBookingByItemId(i.getId());
                if (lastBooking == null) {
                    i.setLastBooking(null);
                } else {
                    i.setLastBooking(new BookingShort(lastBooking.getId(), lastBooking.getBooker().getId()));
                }
                if (nextBooking == null) {
                    i.setNextBooking(null);
                } else {
                    i.setNextBooking(new BookingShort(nextBooking.getId(), nextBooking.getBooker().getId()));
                }
            }

            return items;
        }
    }

    @Override
    public Collection<ItemDto> getSearchedItems(String searchText) {
        if (searchText.isEmpty() || searchText.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(searchText, searchText)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    //update
    @Override
    public ItemDto updateItem(int itemId, ItemDto itemDto, int ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item id = " + itemId + " doesn't exist."));
        Item updatedItem = ItemMapper.toItem(itemDto);
        if (updatedItem.getName() != null) {
            item.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            item.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getAvailable() != null) {
            item.setAvailable(updatedItem.getAvailable());
        }
        if (item.getOwner() == null) {
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new NoSuchElementException("User id = " + ownerId + " doesn't exist."));
            item.setOwner(owner);
        } else {
            if (item.getOwner().getId() != ownerId) {
                throw new ForbiddenActionException("Changing owner is forbidden.");
            }
        }
        item.setId(itemId);

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    //delete
    @Override
    public void deleteItem(int itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NoSuchElementException("Item id = " + itemId + " doesn't exist.");
        }

        itemRepository.deleteById(itemId);
    }

    @Override
    public void deleteAllItems() {
        itemRepository.deleteAll();
    }
}
