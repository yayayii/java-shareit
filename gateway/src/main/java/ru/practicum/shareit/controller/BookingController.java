package ru.practicum.shareit.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.util.RequestState;
import ru.practicum.shareit.dto.request.BookingRequestDto;
import ru.practicum.shareit.client.BookingClient;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@AllArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;


    //create
    @PostMapping
    public ResponseEntity<Object> addBooking(
            @RequestBody @Valid BookingRequestDto bookingDto,
            @RequestHeader("X-Sharer-User-Id") Long bookerId
    ) {
        log.info("gateway BookingController - addBooking");
        ResponseEntity<Object> responseEntity = bookingClient.addBooking(bookingDto, bookerId);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    //read
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("gateway BookingController - getBooking");
        ResponseEntity<Object> responseEntity = bookingClient.getBooking(bookingId, userId);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam(defaultValue = "ALL") RequestState state,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "999") @Positive int size
    ) {
        log.info("gateway BookingController - getAllBookings");
        ResponseEntity<Object> responseEntity = bookingClient.getAllBookings(bookerId, state, from, size);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsFromOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(defaultValue = "ALL") RequestState state,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "999") @Positive int size
    ) {
        log.info("gateway BookingController - getAllBookingsFromOwner");
        ResponseEntity<Object> responseEntity = bookingClient.getAllBookingsFromOwner(ownerId, state, from, size);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    //update
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam("approved") boolean isApproved
    ) {
        log.info("gateway BookingController - updateBooking");
        ResponseEntity<Object> responseEntity = bookingClient.updateBooking(bookingId, ownerId, isApproved);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }
}
