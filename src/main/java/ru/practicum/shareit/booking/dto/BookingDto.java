package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private int id;
    @NotNull @Future
    private LocalDateTime start;
    @NotNull @Future
    private LocalDateTime end;
    private BookingStatus status;
    @JsonProperty("item")
    private ItemDto itemDto;
    @JsonProperty("booker")
    private UserDto userDto;

    @JsonProperty("itemId")
    public void setItem(int itemId) {
        itemDto = new ItemDto();
        itemDto.setId(itemId);
    }
}
