package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    //create
    @PostMapping
    public BookingResponseDto addBooking(
            @Valid @RequestBody BookingRequestDto bookingDto,
            @RequestHeader("X-Sharer-User-Id") int bookerId
    ) {
        return bookingService.addBooking(bookingDto, bookerId);
    }

    //read
    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(
            @PathVariable int bookingId,
            @RequestHeader("X-Sharer-User-Id") int userId
    ) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") int bookerId,
            @RequestParam(name = "state", defaultValue = "ALL") RequestState state,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "999") @Min(1) int size
    ) {
        return bookingService.getAllBookings(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsFromOwner(
            @RequestHeader("X-Sharer-User-Id") int ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") RequestState state,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "999") @Min(1) int size
    ) {
        return bookingService.getAllBookingsFromOwner(ownerId, state, from, size);
    }

    //update
    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBooking(
            @PathVariable int bookingId,
            @RequestHeader("X-Sharer-User-Id") int ownerId,
            @RequestParam("approved") boolean isApproved
    ) {
        return bookingService.updateBooking(bookingId, ownerId, isApproved);
    }

    //delete
}
