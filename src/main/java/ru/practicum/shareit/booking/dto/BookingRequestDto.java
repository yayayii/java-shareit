package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.valid.StartBeforeEndDateValid;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@StartBeforeEndDateValid
public class BookingRequestDto {
    private final int itemId;
    @FutureOrPresent
    private final LocalDateTime start;
    private final LocalDateTime end;
}
