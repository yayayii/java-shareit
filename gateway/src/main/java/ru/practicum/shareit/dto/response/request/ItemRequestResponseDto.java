package ru.practicum.shareit.dto.response.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}
