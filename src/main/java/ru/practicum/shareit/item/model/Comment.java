package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "content")
    private String text;
    @ManyToOne
    private Item item;
    @ManyToOne
    private User author;
    private LocalDateTime created;

    public Comment(String text) {
        this.text = text;
    }
}
