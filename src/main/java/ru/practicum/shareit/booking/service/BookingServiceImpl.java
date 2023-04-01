package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    //create
    @Transactional
    @Override
    public BookingDto addBooking(BookingDto bookingDto, int bookerId) {
        Item item = itemRepository.findById(bookingDto.getItemDto().getId())
                .orElseThrow(() -> new NoSuchElementException("Item id = " + bookingDto.getItemDto().getId() + " doesn't exist."));
        if (!item.getAvailable()) {
            throw new ValidationException("Item id = " + bookingDto.getItemDto().getId() + " isn't available.");
        }
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NoSuchElementException("User id = " + bookerId + " doesn't exist."));

        Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
        if (booking.getBooker().getId() == item.getOwner().getId()) {
            throw new NoSuchElementException("Booker id = " + booking.getBooker().getId() +
                    " is the owner of the item id = " + item.getOwner().getId());
        }
        List<Booking> list = bookingRepository.findBookingsByItem_IdAndStatusIn(
                booking.getItem().getId(), Set.of(BookingStatus.WAITING, BookingStatus.APPROVED), Sort.by(Sort.Direction.DESC, "start"));
        for (Booking otherBooking : list) {
            if (booking.getStart().isAfter(otherBooking.getStart()) && booking.getStart().isBefore(otherBooking.getEnd()) ||
            booking.getEnd().isAfter(otherBooking.getStart()) && booking.getEnd().isBefore(otherBooking.getEnd())) {
                throw new ValidationException("Booking time for this item is intersected with other's booking time for this item " +
                        "(" + booking.getStart() + " : " + booking.getEnd() + ").");
            }
        }
        booking.setStatus(BookingStatus.WAITING);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    //read
    @Override
    public BookingDto getBooking(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking id = " + bookingId + " doesn't exist."));
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new NoSuchElementException("User id = " + userId + " can't get this booking.");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getAllBookings(int userId, RequestState state, boolean isOwner) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User id = " + userId + " doesn't exist.");
        }
        if (!isOwner) {
            switch (state) {
                case ALL:
                    return bookingRepository.findBookingsByBooker_Id(userId, Sort.by(Sort.Direction.DESC, "start"))
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
                case PAST:
                    return bookingRepository.findPastBookings(userId, Sort.by(Sort.Direction.DESC, "start"))
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
                case FUTURE:
                    return bookingRepository.findFutureBookings(userId, Sort.by(Sort.Direction.DESC, "start"))
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
                case CURRENT:
                    return bookingRepository.findCurrentBookings(userId, Sort.by(Sort.Direction.DESC, "start"))
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
                case WAITING:
                case REJECTED:
                    return bookingRepository.findBookingsByBooker_IdAndStatus(userId, BookingStatus.valueOf(String.valueOf(state)), Sort.by(Sort.Direction.DESC, "start"))
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
                default:
                    throw new ValidationException("Unknown state: " + state);
            }
        } else {
            switch (state) {
                case ALL:
                    return bookingRepository.findBookingsByItem_Owner_Id(userId, Sort.by(Sort.Direction.DESC, "start"))
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
                case PAST:
                    return bookingRepository.findPastBookingsByItem_Owner_Id(userId, Sort.by(Sort.Direction.DESC, "start"))
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
                case FUTURE:
                    return bookingRepository.findFutureBookingsByItem_Owner_Id(userId, Sort.by(Sort.Direction.DESC, "start"))
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
                case CURRENT:
                    return bookingRepository.findCurrentBookingsByItem_Owner_Id(userId, Sort.by(Sort.Direction.DESC, "start"))
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
                case WAITING:
                case REJECTED:
                    return bookingRepository.findBookingsByItem_Owner_IdAndStatus(userId, BookingStatus.valueOf(String.valueOf(state)), Sort.by(Sort.Direction.DESC, "start"))
                            .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
                default:
                    throw new ValidationException("Unknown state: " + state);
            }
        }
    }

    //update
    @Transactional
    @Override
    public BookingDto updateBooking(int bookingId, int ownerId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking with id = " + bookingId + " doesn't exist."));
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new NoSuchElementException("User id = " + ownerId + " isn't an owner of the item.");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new IllegalArgumentException("Changing status after approving is forbidden.");
        }
        if (isApproved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingDto(booking);
    }

    //delete
}
