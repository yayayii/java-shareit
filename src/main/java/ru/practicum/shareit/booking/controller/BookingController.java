package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
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
    BookingResponseDto addBooking(
            @Valid @RequestBody BookingRequestDto bookingDto,
            @RequestHeader("X-Sharer-User-Id") int bookerId
    ) {
        return bookingService.addBooking(bookingDto, bookerId);
    }

    //read
    @GetMapping("/{bookingId}")
    BookingResponseDto getBooking(
            @PathVariable int bookingId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    Collection<BookingResponseDto> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam(name = "state", defaultValue = "ALL") RequestState state
    ) {
        return bookingService.getAllBookings(userId, state);
    }

    @GetMapping("/owner")
    Collection<BookingResponseDto> getAllBookingsFromOwner(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam(name = "state", defaultValue = "ALL") RequestState state
    ) {
        return bookingService.getAllBookingsFromOwner(userId, state);
    }

    //update
    @PatchMapping("/{bookingId}")
    BookingResponseDto updateBooking(
            @PathVariable int bookingId,
            @RequestHeader("X-Sharer-User-Id") int ownerId,
            @RequestParam("approved") boolean isApproved
    ) {
        return bookingService.updateBooking(bookingId, ownerId, isApproved);
    }

    //delete
}
