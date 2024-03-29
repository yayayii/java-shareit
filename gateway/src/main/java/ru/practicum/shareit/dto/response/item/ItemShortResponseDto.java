package ru.practicum.shareit.dto.response.item;

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
