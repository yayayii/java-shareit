package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "user_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
