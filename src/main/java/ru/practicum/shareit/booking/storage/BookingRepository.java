package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingsByBooker_IdOrderByStartDesc(int bookerId);

    @Query("select b  from Booking b " +
            "join b.booker u " +
            "where u.id = ?1 " +
            "and current_timestamp between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findCurrentBookings(int bookerId);

    @Query("select b  from Booking b " +
            "join b.booker u " +
            "where u.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findPastBookings(int bookerId);

    @Query("select b  from Booking b " +
            "join b.booker u " +
            "where u.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> findFutureBookings(int bookerId);

    List<Booking> findBookingsByBooker_IdAndStatusOrderByStartDesc(int bookerId, BookingStatus status);


    List<Booking> findBookingsByItem_Owner_IdOrderByStartDesc(int ownerId);

    @Query("select b  from Booking b " +
            "join b.item i " +
            "where i.owner.id = ?1 " +
            "and current_timestamp between b.start and b.end " +
            "order by b.start desc")
    List<Booking> findCurrentBookingsByItem_Owner_Id(int ownerId);

    @Query("select b  from Booking b " +
            "join b.item i " +
            "where i.owner.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findPastBookingsByItem_Owner_Id(int ownerId);

    @Query("select b  from Booking b " +
            "join b.item i " +
            "where i.owner.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> findFutureBookingsByItem_Owner_Id(int ownerId);

    List<Booking> findBookingsByItem_Owner_IdAndStatusOrderByStartDesc(int ownerId, BookingStatus status);

    List<Booking> findBookingsByItem_IdAndStatusIn(int itemId, Set<BookingStatus> set);


    @Query(value = "select * from booking b " +
            "where item_id = ?1 " +
            "and end_date < current_timestamp " +
            "order by end_date desc " +
            "limit 1",
    nativeQuery = true)
    Booking findLastBookingByItemId(int itemId);

    @Query(value = "select * from booking b " +
            "where item_id = ?1 " +
            "and start_date > current_timestamp " +
            "order by start_date " +
            "limit 1",
            nativeQuery = true)
    Booking findNextBookingByItemId(int itemId);
}
