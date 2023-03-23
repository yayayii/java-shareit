package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {
    //create
    BookingDto addBooking(BookingDto bookingDto, int bookerId);

    //read
    BookingDto getBooking(int bookingId, int userId);

    Collection<BookingDto> getAllBookings(int userId, String state, boolean isOwner);

    //update
    BookingDto updateBooking(int bookingId, int ownerId, boolean isApproved);

    //delete
}
