package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private int id;
    @NotNull @NotBlank @NotEmpty
    private String text;
    private String authorName;
    private LocalDateTime created;
}
