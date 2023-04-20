package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;


@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;


    //create
    @PostMapping
    public ResponseEntity<BookingResponseDto> addBooking(
            @RequestBody BookingRequestDto bookingDto,
            @RequestHeader("X-Sharer-User-Id") Long bookerId
    ) {
        log.info("server BookingController - addBooking");
        return ResponseEntity.ok(bookingService.addBooking(bookingDto, bookerId));
    }

    //read
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> getBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("server BookingController - getBooking");
        return ResponseEntity.ok(bookingService.getBooking(bookingId, userId));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam RequestState state,
            @RequestParam int from,
            @RequestParam int size
    ) {
        log.info("server BookingController - getAllBookings");
        return ResponseEntity.ok(bookingService.getAllBookings(bookerId, state, from, size));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDto>> getAllBookingsFromOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam RequestState state,
            @RequestParam int from,
            @RequestParam int size
    ) {
        log.info("server BookingController - getAllBookingsFromOwner");
        return ResponseEntity.ok(bookingService.getAllBookingsFromOwner(ownerId, state, from, size));
    }

    //update
    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> updateBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam boolean isApproved
    ) {
        log.info("server BookingController - updateBooking");
        return ResponseEntity.ok(bookingService.updateBooking(bookingId, ownerId, isApproved));
    }
}
