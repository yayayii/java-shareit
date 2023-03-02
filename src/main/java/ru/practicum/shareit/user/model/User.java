package ru.practicum.shareit.user.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    private String name;
    private String email;
    private Set<Integer> itemIds = new HashSet<>();

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
