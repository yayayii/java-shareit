package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.RequestState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.item.ItemShortResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserShortResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository mockBookingRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ItemRepository mockItemRepository;
    private BookingService bookingService;


    private static LocalDateTime testLocalDateTime;

    private static UserShortResponseDto[] testUserShortResponseDtos;
    private static User[] testUsers;

    private static ItemShortResponseDto[] testItemShortResponseDtos;
    private static Item[] testItems;

    private static BookingRequestDto testBookingRequestDto;
    private static Booking[] testBookings;
    private static BookingResponseDto[] testBookingResponseDtos;


    @BeforeAll
    static void beforeAll() {
        testLocalDateTime = LocalDateTime.of(2001, 1, 1, 1, 1);
        initTestUser();
        initTestItem();
        initTestBooking();
    }

    @BeforeEach
    void beforeEach() {
        bookingService = new BookingServiceImpl(mockBookingRepository, mockUserRepository, mockItemRepository);
    }


    @Test
    void testAddBooking() {
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.addBooking(testBookingRequestDto, 1L)
        );
        assertEquals(
                "Item id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItems[0]));
        exception = assertThrows(
                ValidationException.class,
                () -> bookingService.addBooking(testBookingRequestDto, 1L)
        );
        assertEquals(
                "Item id = 1 isn't available.",
                exception.getMessage()
        );
        testItems[0].setAvailable(true);

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.addBooking(testBookingRequestDto, 1L)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUsers[0]));
        exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.addBooking(testBookingRequestDto, 1L)
        );
        assertEquals(
                "Booker id = 1 is the owner of the item id = 1.",
                exception.getMessage()
        );

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUsers[1]));
        when(mockBookingRepository.findIntersectedBookingByItemId(anyLong(), any(), any()))
                .thenReturn(testBookings[0]);
        exception = assertThrows(
                ValidationException.class,
                () -> bookingService.addBooking(testBookingRequestDto, 1L)
        );
        assertEquals(
                "Booking time for this item is intersected with other's booking time for this item " +
                        "(" + testLocalDateTime + " : " + testLocalDateTime + ").",
                exception.getMessage()
        );

        when(mockBookingRepository.findIntersectedBookingByItemId(anyLong(), any(), any()))
                .thenReturn(null);
        when(mockBookingRepository.save(any()))
                .thenReturn(testBookings[0]);
        Assertions.assertDoesNotThrow(
                () -> bookingService.addBooking(testBookingRequestDto, 1L)
        );
    }

    @Test
    void testGetBooking() {
        when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.getBooking(1L, 1L)
        );
        assertEquals(
                "Booking id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBookings[0]));
        exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.getBooking(1L, 2L)
        );
        assertEquals(
                "User id = 2 can't get this booking.",
                exception.getMessage()
        );

        Assertions.assertDoesNotThrow(
                () -> bookingService.getBooking(1L, 1L)
        );
    }

    @Test
    void testGetAllBookings() {
        when(mockUserRepository.existsById(anyLong()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.getAllBookings(1L, RequestState.ALL, 1, 2)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.existsById(anyLong()))
                .thenReturn(true);
        when(mockBookingRepository.findBookingsByBooker_Id(anyLong(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookings(1L, RequestState.ALL, 0, 1)
        );

        when(mockBookingRepository.findPastBookingsByBooker_Id(anyLong(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookings(1L, RequestState.PAST, 0, 1)
        );

        when(mockBookingRepository.findFutureBookingsByBooker_Id(anyLong(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookings(1L, RequestState.FUTURE, 0, 1)
        );

        when(mockBookingRepository.findCurrentBookingsByBooker_Id(anyLong(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookings(1L, RequestState.CURRENT, 0, 1)
        );

        when(mockBookingRepository.findBookingsByBooker_IdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookings(1L, RequestState.WAITING, 0, 1)
        );

        when(mockBookingRepository.findBookingsByBooker_IdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookings(1L, RequestState.REJECTED, 0, 1)
        );
    }

    @Test
    void testGetAllBookingsFromOwner() {
        when(mockUserRepository.existsById(anyLong()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.getAllBookingsFromOwner(1L, RequestState.ALL, 1, 2)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.existsById(anyLong()))
                .thenReturn(true);
        when(mockBookingRepository.findBookingsByItem_Owner_Id(anyLong(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookingsFromOwner(1L, RequestState.ALL, 0, 1)
        );

        when(mockBookingRepository.findPastBookingsByItem_Owner_Id(anyLong(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookingsFromOwner(1L, RequestState.PAST, 0, 1)
        );

        when(mockBookingRepository.findFutureBookingsByItem_Owner_Id(anyLong(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookingsFromOwner(1L, RequestState.FUTURE, 0, 1)
        );

        when(mockBookingRepository.findCurrentBookingsByItem_Owner_Id(anyLong(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookingsFromOwner(1L, RequestState.CURRENT, 0, 1)
        );

        when(mockBookingRepository.findBookingsByItem_Owner_IdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookingsFromOwner(1L, RequestState.WAITING, 0, 1)
        );

        when(mockBookingRepository.findBookingsByItem_Owner_IdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(testBookings[0], testBookings[1]));
        assertEquals(
                List.of(testBookingResponseDtos[0], testBookingResponseDtos[1]),
                bookingService.getAllBookingsFromOwner(1L, RequestState.REJECTED, 0, 1)
        );
    }

    @Test
    void testUpdateBooking() {
        when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.updateBooking(1L, 1L, true)
        );
        assertEquals(
                "Booking id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(testBookings[0]));
        exception = assertThrows(
                NoSuchElementException.class,
                () -> bookingService.updateBooking(1L, 2L, true)
        );
        assertEquals(
                "User id = 2 isn't an owner of the item.",
                exception.getMessage()
        );

        testBookings[0].setStatus(BookingStatus.APPROVED);
        exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.updateBooking(1L, 1L, true)
        );
        assertEquals(
                "Changing status after approving is forbidden.",
                exception.getMessage()
        );

        testBookings[0].setStatus(BookingStatus.WAITING);
        assertEquals(
                new BookingResponseDto(
                        1L,
                        testLocalDateTime,
                        testLocalDateTime,
                        BookingStatus.APPROVED,
                        testUserShortResponseDtos[0],testItemShortResponseDtos[0]
                ),
                bookingService.updateBooking(1L, 1L, true)
        );
        testBookings[0].setStatus(BookingStatus.WAITING);

        assertEquals(
                new BookingResponseDto(
                        1L,
                        testLocalDateTime,
                        testLocalDateTime,
                        BookingStatus.REJECTED,
                        testUserShortResponseDtos[0],
                        testItemShortResponseDtos[0]
                ),
                bookingService.updateBooking(1L, 1L, false)
        );
        testBookings[0].setStatus(BookingStatus.WAITING);
    }


    private static void initTestUser() {
        testUserShortResponseDtos = new UserShortResponseDto[3];
        testUserShortResponseDtos[0] = new UserShortResponseDto(1L);
        testUserShortResponseDtos[1] = new UserShortResponseDto(2L);

        testUsers = new User[2];
        testUsers[0] = new User(1L, "UserName1", "UserEmail1");
        testUsers[1] = new User(2L, "UserName2", "UserEmail2");
    }

    private static void initTestItem() {
        testItemShortResponseDtos = new ItemShortResponseDto[2];
        testItemShortResponseDtos[0] = new ItemShortResponseDto(1L, "ItemName1");
        testItemShortResponseDtos[1] = new ItemShortResponseDto(2L, "ItemName2");

        testItems = new Item[2];
        testItems[0] = new Item(
                1L, "ItemName1", "ItemDescription1", false, testUsers[0], null
        );
        testItems[1] = new Item(
                2L, "ItemName2", "ItemDescription2", true, testUsers[1], null
        );
    }

    private static void initTestBooking() {
        testBookingRequestDto = new BookingRequestDto(1L, testLocalDateTime, testLocalDateTime);

        testBookings = new Booking[2];
        testBookings[0] = new Booking(
                1L, testLocalDateTime, testLocalDateTime, BookingStatus.WAITING, testUsers[0], testItems[0]
        );
        testBookings[1] = new Booking(
                2L, testLocalDateTime, testLocalDateTime, BookingStatus.WAITING, testUsers[1], testItems[1]
        );

        testBookingResponseDtos = new BookingResponseDto[2];
        testBookingResponseDtos[0] = new BookingResponseDto(
                1L, testLocalDateTime, testLocalDateTime, BookingStatus.WAITING, testUserShortResponseDtos[0], testItemShortResponseDtos[0]
        );
        testBookingResponseDtos[1] = new BookingResponseDto(
                2L, testLocalDateTime, testLocalDateTime, BookingStatus.WAITING, testUserShortResponseDtos[1], testItemShortResponseDtos[1]
        );
    }
}
