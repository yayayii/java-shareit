package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;

import java.util.List;

@Data
public class ItemResponseDto {
    private final int id;
    private final String name;
    private final String description;
    private final Boolean available;
    private BookingShortResponseDto lastBooking;
    private BookingShortResponseDto nextBooking;
    private List<CommentResponseDto> comments;
}
