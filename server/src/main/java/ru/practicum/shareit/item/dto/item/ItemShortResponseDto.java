package ru.practicum.shareit.item.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemShortResponseDto {
    private Long id;
    private String name;
}
