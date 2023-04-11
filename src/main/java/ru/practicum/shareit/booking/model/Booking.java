package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;
    @ManyToOne
    private User booker;
    @ManyToOne
    private Item item;

    public Booking(LocalDateTime start, LocalDateTime end, User booker, Item item) {
        this.start = start;
        this.end = end;
        this.booker = booker;
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(start, booking.start) && Objects.equals(end, booking.end) && Objects.equals(booker, booking.booker) && Objects.equals(item, booking.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, status, booker, item);
    }
}
