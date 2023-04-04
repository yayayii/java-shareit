package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.RequestState;

import java.util.Collection;

public interface BookingService {
    //create
    BookingResponseDto addBooking(BookingRequestDto bookingDto, int bookerId);

    //read
    BookingResponseDto getBooking(int bookingId, int userId);

    Collection<BookingResponseDto> getAllBookings(int userId, RequestState state);

    Collection<BookingResponseDto> getAllBookingsFromOwner(int userId, RequestState state);

    //update
    BookingResponseDto updateBooking(int bookingId, int ownerId, boolean isApproved);

    //delete
}
