package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @OneToOne
    private User owner;
    @OneToOne
    private ItemRequest request;

    public Item(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
