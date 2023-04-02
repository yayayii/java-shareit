package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.user.dto.UserShortResponseDto;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private final int id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final BookingStatus status;
    private final UserShortResponseDto booker;
    private final ItemShortResponseDto item;
}
