package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingsByBooker_Id(int bookerId, Sort sort);

    @Query("select b  from Booking b " +
            "join b.booker u " +
            "where u.id = ?1 " +
            "and current_timestamp between b.start and b.end")
    List<Booking> findCurrentBookings(int bookerId, Sort sort);

    @Query("select b  from Booking b " +
            "join b.booker u " +
            "where u.id = ?1 " +
            "and b.end < current_timestamp")
    List<Booking> findPastBookings(int bookerId, Sort sort);

    @Query("select b  from Booking b " +
            "join b.booker u " +
            "where u.id = ?1 " +
            "and b.start > current_timestamp")
    List<Booking> findFutureBookings(int bookerId, Sort sort);

    List<Booking> findBookingsByBooker_IdAndStatus(int bookerId, BookingStatus status, Sort sort);


    List<Booking> findBookingsByItem_Owner_Id(int ownerId, Sort sort);

    @Query("select b  from Booking b " +
            "join b.item i " +
            "where i.owner.id = ?1 " +
            "and current_timestamp between b.start and b.end")
    List<Booking> findCurrentBookingsByItem_Owner_Id(int ownerId, Sort sort);

    @Query("select b  from Booking b " +
            "join b.item i " +
            "where i.owner.id = ?1 " +
            "and b.end < current_timestamp")
    List<Booking> findPastBookingsByItem_Owner_Id(int ownerId, Sort sort);

    @Query("select b  from Booking b " +
            "join b.item i " +
            "where i.owner.id = ?1 " +
            "and b.start > current_timestamp")
    List<Booking> findFutureBookingsByItem_Owner_Id(int ownerId, Sort sort);

    List<Booking> findBookingsByItem_Owner_IdAndStatus(int ownerId, BookingStatus status, Sort sort);

    List<Booking> findBookingsByItem_IdAndStatusIn(int itemId, Set<BookingStatus> set, Sort sort);


    @Query(value = "select * from booking b " +
            "where item_id = ?1 " +
            "and start_date < current_timestamp " +
            "and status in ('WAITING', 'APPROVED')" +
            "order by end_date desc " +
            "limit 1",
    nativeQuery = true)
    Booking findLastBookingByItemId(int itemId);

    @Query(value = "select * from booking b " +
            "where item_id = ?1 " +
            "and start_date > current_timestamp " +
            "and status in ('WAITING', 'APPROVED')" +
            "order by start_date " +
            "limit 1",
            nativeQuery = true)
    Booking findNextBookingByItemId(int itemId);

    @Query(value = "select * from booking b " +
            "where item_id = ?1 " +
            "and booker_id = ?2 " +
            "and end_date < current_timestamp " +
            "and status like 'APPROVED' " +
            "order by end_date desc " +
            "limit 1",
            nativeQuery = true)
    Booking findLastBookingByItemIdAndBookerId(int itemId, int userId);
}
