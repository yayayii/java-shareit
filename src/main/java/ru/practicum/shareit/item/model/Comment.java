package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name = "content")
    private String text;
    @OneToOne
    private Item item;
    @OneToOne
    private User author;
    private LocalDateTime created;
}
