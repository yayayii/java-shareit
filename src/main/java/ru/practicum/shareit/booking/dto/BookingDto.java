package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.booking.valid.StartBeforeEndDateValid;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@StartBeforeEndDateValid
public class BookingDto {
    private int id;
    @NotNull @FutureOrPresent
    private LocalDateTime start;
    @NotNull @Future
    private LocalDateTime end;
    private BookingStatus status;
    @JsonProperty("item")
    private ItemResponseDto itemDto;
    @JsonProperty("booker")
    private UserDto userDto;

    @JsonProperty("itemId")
    public void setItem(int itemId) {
        itemDto = new ItemResponseDto();
        itemDto.setId(itemId);
    }
}
