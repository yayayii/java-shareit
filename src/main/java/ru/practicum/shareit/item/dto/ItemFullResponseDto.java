package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemFullResponseDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingShortResponseDto lastBooking;
    private BookingShortResponseDto nextBooking;
    private List<CommentResponseDto> comments;

    public ItemFullResponseDto(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}