package ru.practicum.shareit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestFullResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemResponseDto> items;
}
