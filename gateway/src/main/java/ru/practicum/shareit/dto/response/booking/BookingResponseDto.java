package ru.practicum.shareit.dto.response.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.dto.response.item.ItemShortResponseDto;
import ru.practicum.shareit.dto.response.user.UserShortResponseDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private UserShortResponseDto booker;
    private ItemShortResponseDto item;
}
