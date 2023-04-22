package ru.practicum.shareit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestRequestDto {
    @NotBlank
    private String description;
}
