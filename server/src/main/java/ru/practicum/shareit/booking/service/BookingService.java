package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.RequestState;

import java.util.List;


public interface BookingService {
    //create
    BookingResponseDto addBooking(BookingRequestDto bookingDto, Long bookerId);

    //read
    BookingResponseDto getBooking(Long bookingId, Long userId);

    List<BookingResponseDto> getAllBookings(Long bookerId, RequestState state, int from, int size);

    List<BookingResponseDto> getAllBookingsFromOwner(Long ownerId, RequestState state, int from, int size);

    //update
    BookingResponseDto updateBooking(Long bookingId, Long ownerId, boolean isApproved);
}
