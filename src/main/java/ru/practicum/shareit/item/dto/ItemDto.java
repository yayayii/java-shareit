package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
public class ItemDto {
    private int id;
    private final String name;
    private final String description;
    private final boolean available;
    private User owner;
    private final Integer request;
}
