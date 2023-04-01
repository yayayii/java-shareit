package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    //create
    @PostMapping
    BookingDto addBooking(
            @Valid @RequestBody BookingDto bookingDto,
            @RequestHeader("X-Sharer-User-Id") int bookerId
    ) {
        return bookingService.addBooking(bookingDto, bookerId);
    }

    //read
    @GetMapping("/{bookingId}")
    BookingDto getBooking(
            @PathVariable int bookingId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    Collection<BookingDto> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") RequestState state
    ) {
        return bookingService.getAllBookings(userId, state, false);
    }

    @GetMapping("/owner")
    Collection<BookingDto> getAllBookingsFromOwner(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") RequestState state
    ) {
        return bookingService.getAllBookings(userId, state, true);
    }

    //update
    @PatchMapping("/{bookingId}")
    BookingDto updateBooking(
            @PathVariable int bookingId,
            @RequestHeader("X-Sharer-User-Id") int ownerId,
            @RequestParam("approved") boolean isApproved
    ) {
        return bookingService.updateBooking(bookingId, ownerId, isApproved);
    }

    //delete
}
