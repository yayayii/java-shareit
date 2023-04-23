package ru.practicum.shareit.dto.response.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingShortResponseDto {
    private Long id;
    private Long bookerId;
}
