package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;

import java.util.List;
import java.util.NoSuchElementException;
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
    public BookingResponseDto addBooking(BookingRequestDto bookingDto, Long bookerId) {
        log.info("server BookingService - addBooking");

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NoSuchElementException("Item id = " + bookingDto.getItemId() + " doesn't exist."));
        if (!item.getAvailable()) {
            throw new ValidationException("Item id = " + item.getId() + " isn't available.");
        }
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NoSuchElementException("User id = " + bookerId + " doesn't exist."));

        Booking booking = BookingMapper.toBooking(bookingDto, booker, item);
        if (booking.getBooker().getId() == item.getOwner().getId()) {
            throw new NoSuchElementException("Booker id = " + booking.getBooker().getId() +
                    " is the owner of the item id = " + item.getOwner().getId() + ".");
        }
        Booking intersectedBooking = bookingRepository
                .findIntersectedBookingByItemId(booking.getItem().getId(), booking.getStart(), booking.getEnd());
        if (intersectedBooking != null) {
            throw new ValidationException("Booking time for this item is intersected with other's booking time for this item " +
                        "(" + intersectedBooking.getStart() + " : " + intersectedBooking.getEnd() + ").");
        }
        booking.setStatus(BookingStatus.WAITING);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    //read
    @Override
    public BookingResponseDto getBooking(Long bookingId, Long userId) {
        log.info("server BookingService - getBooking");

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking id = " + bookingId + " doesn't exist."));
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new NoSuchElementException("User id = " + userId + " can't get this booking.");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAllBookings(Long bookerId, RequestState state, int from, int size) {
        log.info("server BookingService - getAllBookings");

        if (!userRepository.existsById(bookerId)) {
            throw new NoSuchElementException("User id = " + bookerId + " doesn't exist.");
        }

        switch (state) {
            case ALL:
                return bookingRepository.findBookingsByBooker_Id(bookerId, PageRequest.of(from / size, size, Sort.by("start").descending()))
                        .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case PAST:
                return bookingRepository.findPastBookingsByBooker_Id(bookerId, PageRequest.of(from / size, size, Sort.by("start").descending()))
                        .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findFutureBookingsByBooker_Id(bookerId, PageRequest.of(from / size, size, Sort.by("start").descending()))
                        .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findCurrentBookingsByBooker_Id(bookerId, PageRequest.of(from / size, size, Sort.by("start").descending()))
                        .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case WAITING:
            case REJECTED:
                return bookingRepository.findBookingsByBooker_IdAndStatus(bookerId, BookingStatus.valueOf(String.valueOf(state)), PageRequest.of(from / size, size, Sort.by("start").descending()))
                        .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: " + state + ".");
        }
    }

    @Override
    public List<BookingResponseDto> getAllBookingsFromOwner(Long ownerId, RequestState state, int from, int size) {
        log.info("server BookingService - getAllBookingsFromOwner");

        if (!userRepository.existsById(ownerId)) {
            throw new NoSuchElementException("User id = " + ownerId + " doesn't exist.");
        }

        switch (state) {
            case ALL:
                return bookingRepository.findBookingsByItem_Owner_Id(ownerId, PageRequest.of(from / size, size, Sort.by("start").descending()))
                        .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case PAST:
                return bookingRepository.findPastBookingsByItem_Owner_Id(ownerId, PageRequest.of(from / size, size, Sort.by("start").descending()))
                        .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findFutureBookingsByItem_Owner_Id(ownerId, PageRequest.of(from / size, size, Sort.by("start").descending()))
                        .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findCurrentBookingsByItem_Owner_Id(ownerId, PageRequest.of(from / size, size, Sort.by("start").descending()))
                        .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            case WAITING:
            case REJECTED:
                return bookingRepository.findBookingsByItem_Owner_IdAndStatus(ownerId, BookingStatus.valueOf(String.valueOf(state)), PageRequest.of(from / size, size, Sort.by("start").descending()))
                        .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: " + state);
        }
    }

    //update
    @Transactional
    @Override
    public BookingResponseDto updateBooking(Long bookingId, Long ownerId, boolean isApproved) {
        log.info("server BookingService - updateBooking");

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking id = " + bookingId + " doesn't exist."));
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
}
