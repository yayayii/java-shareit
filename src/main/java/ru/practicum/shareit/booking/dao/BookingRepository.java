package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingsByBooker_Id(int bookerId, Pageable pageable);

    @Query(
            "select b  from Booking b " +
            "join b.booker u " +
            "where u.id = ?1 " +
            "and current_timestamp between b.start and b.end"
    )
    List<Booking> findCurrentBookingsByBooker_Id(int bookerId, Pageable pageable);

    @Query(
            "select b  from Booking b " +
            "join b.booker u " +
            "where u.id = ?1 " +
            "and b.end < current_timestamp"
    )
    List<Booking> findPastBookingsByBooker_Id(int bookerId, Pageable pageable);

    @Query(
            "select b  from Booking b " +
            "join b.booker u " +
            "where u.id = ?1 " +
            "and b.start > current_timestamp"
    )
    List<Booking> findFutureBookingsByBooker_Id(int bookerId, Pageable pageable);

    List<Booking> findBookingsByBooker_IdAndStatus(int bookerId, BookingStatus status, Pageable pageable);


    List<Booking> findBookingsByItem_Owner_Id(int ownerId, Pageable pageable);

    @Query(
            "select b  from Booking b " +
            "join b.item i " +
            "where i.owner.id = ?1 " +
            "and current_timestamp between b.start and b.end"
    )
    List<Booking> findCurrentBookingsByItem_Owner_Id(int ownerId, Pageable pageable);

    @Query(
            "select b  from Booking b " +
            "join b.item i " +
            "where i.owner.id = ?1 " +
            "and b.end < current_timestamp"
    )
    List<Booking> findPastBookingsByItem_Owner_Id(int ownerId, Pageable pageable);

    @Query(
            "select b  from Booking b " +
            "join b.item i " +
            "where i.owner.id = ?1 " +
            "and b.start > current_timestamp"
    )
    List<Booking> findFutureBookingsByItem_Owner_Id(int ownerId, Pageable pageable);

    List<Booking> findBookingsByItem_Owner_IdAndStatus(int ownerId, BookingStatus status, Pageable pageable);


    @Query(
        value = "select * from booking b " +
                "where item_id = ?1 " +
                "and start_date <= current_timestamp " +
                "and status = 'APPROVED' " +
                "order by end_date desc " +
                "limit 1",
        nativeQuery = true
    )
    Booking findLastBookingByItemId(int itemId);

    @Query(
            "select b from Booking b " +
            "where b.start <= current_timestamp " +
            "and b.status = 'APPROVED' " +
            "order by b.end desc"
    )
    List<Booking> findLastBookings();

    @Query(
        value = "select * from booking b " +
                "where item_id = ?1 " +
                "and start_date > current_timestamp " +
                "and status = 'APPROVED' " +
                "order by start_date " +
                "limit 1",
        nativeQuery = true
    )
    Booking findNextBookingByItemId(int itemId);

    @Query(
            "select b from Booking b " +
            "where b.start > current_timestamp " +
            "and b.status = 'APPROVED' " +
            "order by b.start"
    )
    List<Booking> findNextBookings();

    @Query(
        value = "select * from booking b " +
                "where item_id = ?1 " +
                "and booker_id = ?2 " +
                "and end_date < current_timestamp " +
                "and status = 'APPROVED' " +
                "order by end_date desc " +
                "limit 1",
        nativeQuery = true
    )
    Booking findLastBookingByItemIdAndBookerId(int itemId, int userId);

    @Query(
        value = "select * from booking b " +
                "where item_id = ?1 " +
                "and (?2 > start_date and ?2 < end_date " +
                "or ?3 > start_date and ?3 < end_date) " +
                "and status in ('WAITING', 'APPROVED') " +
                "limit 1",
        nativeQuery = true
    )
    Booking findIntersectedBookingByItemId(int itemId, LocalDateTime start, LocalDateTime end);
}
