package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.RequestState;

import java.util.List;

public interface BookingService {
    //create
    BookingResponseDto addBooking(BookingRequestDto bookingDto, int bookerId);

    //read
    BookingResponseDto getBooking(int bookingId, int userId);

    List<BookingResponseDto> getAllBookings(int bookerId, RequestState state, int from, int size);

    List<BookingResponseDto> getAllBookingsFromOwner(int ownerId, RequestState state, int from, int size);

    //update
    BookingResponseDto updateBooking(int bookingId, int ownerId, boolean isApproved);

    //delete
}
