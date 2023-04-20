package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenActionException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.item.ItemFullResponseDto;
import ru.practicum.shareit.item.dto.item.ItemRequestDto;
import ru.practicum.shareit.item.dto.item.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository mockItemRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private BookingRepository mockBookingRepository;
    @Mock
    private CommentRepository mockCommentRepository;
    @Mock
    private ItemRequestRepository mockItemRequestRepository;
    private ItemService itemService;


    private static LocalDateTime testLocalDateTime;
    private static User testUser;
    private static ItemRequest testItemRequest;

    private static ItemRequestDto[] testItemRequestDtos;
    private static Item[] testItems;
    private static ItemResponseDto[] testItemResponseDtos;
    private static ItemFullResponseDto[] testItemFullResponseDtos;

    private static CommentRequestDto testCommentRequestDtos;
    private static Comment[] testComments;
    private static CommentResponseDto[] testCommentResponseDtos;

    private static Booking[] testBookings;
    private static BookingShortResponseDto[] testBookingShortResponseDtos;


    @BeforeAll
    static void beforeAll() {
        testLocalDateTime = LocalDateTime.of(2001, 1, 1, 1, 1);
        testUser = new User(1L, "UserName1", "UserEmail1");
        testItemRequest = new ItemRequest(
                1L, "ItemDescription1", testLocalDateTime, testUser, null
        );
        initTestItem();
        initTestComment();
        initTestBooking();
    }

    @BeforeEach
    void beforeEach() {
        itemService = new ItemServiceImpl(
                mockItemRepository,
                mockUserRepository,
                mockBookingRepository,
                mockCommentRepository,
                mockItemRequestRepository
        );
    }


    @Test
    void testAddItem() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.addItem(testItemRequestDtos[0], 1L)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        when(mockItemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.addItem(testItemRequestDtos[0], 1L)
        );
        assertEquals(
                "Item Request id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockItemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItemRequest));
        when(mockItemRepository.save(any()))
                .thenReturn(testItems[0]);
        Assertions.assertDoesNotThrow(() -> itemService.addItem(testItemRequestDtos[0], 1L));
    }

    @Test
    void testAddComment() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.addComment(testCommentRequestDtos, 1L, 1L)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(testUser));
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.addComment(testCommentRequestDtos, 1L, 1L)
        );
        assertEquals(
                "Item id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItems[0]));
        exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.addComment(testCommentRequestDtos, 1L, 1L)
        );
        assertEquals(
                "Owner of the item can't leave comments on his own items.",
                exception.getMessage()
        );

        when(mockBookingRepository.findLastBookingByItemIdAndBookerId(anyLong(), anyLong()))
                .thenReturn(null);
        exception = assertThrows(
                ValidationException.class,
                () -> itemService.addComment(testCommentRequestDtos, 1L, 2L)
        );
        assertEquals(
                "You must have this item in the past in order to leave a comment.",
                exception.getMessage()
        );

        when(mockBookingRepository.findLastBookingByItemIdAndBookerId(anyLong(), anyLong()))
                .thenReturn(testBookings[0]);
        when(mockCommentRepository.save(any()))
                .thenReturn(testComments[0]);
        Assertions.assertDoesNotThrow(() -> itemService.addComment(testCommentRequestDtos, 1L, 2L));
    }

    @Test
    void testGetItem() {
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.getItem(1L, 1L)
        );
        assertEquals(
                "Item id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItems[0]));
        when(mockCommentRepository.findCommentsByItem_Id(anyLong()))
                .thenReturn(List.of(testComments[0], testComments[1]));
        testItemFullResponseDtos[0].setComments(List.of(testCommentResponseDtos[0], testCommentResponseDtos[1]));
        assertEquals(
                testItemFullResponseDtos[0],
                itemService.getItem(1L, 2L)
        );

        when(mockBookingRepository.findLastBookingByItemId(anyLong()))
                .thenReturn(null);
        when(mockBookingRepository.findLastBookingByItemId(anyLong()))
                .thenReturn(null);
        assertEquals(
                testItemFullResponseDtos[0],
                itemService.getItem(1L, 1L)
        );

        when(mockBookingRepository.findLastBookingByItemId(anyLong()))
                .thenReturn(testBookings[0]);
        when(mockBookingRepository.findNextBookingByItemId(anyLong()))
                .thenReturn(testBookings[1]);
        testItemFullResponseDtos[0].setLastBooking(testBookingShortResponseDtos[0]);
        testItemFullResponseDtos[0].setNextBooking(testBookingShortResponseDtos[1]);
        assertEquals(
                testItemFullResponseDtos[0],
                itemService.getItem(1L, 1L)
        );
        testItemFullResponseDtos[0].setLastBooking(null);
        testItemFullResponseDtos[0].setNextBooking(null);
    }

    @Test
    void testGetAllItems() {
        when(mockUserRepository.existsById(anyLong()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.getAllItems(1L, 0, 1)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.existsById(anyLong()))
                .thenReturn(true);
        when(mockItemRepository.findByOwner_IdOrderById(anyLong(), any()))
                .thenReturn(List.of(testItems[0], testItems[1]));
        testItemFullResponseDtos[0].setComments(Collections.emptyList());
        testItemFullResponseDtos[1].setComments(Collections.emptyList());
        assertEquals(
                List.of(testItemFullResponseDtos[0], testItemFullResponseDtos[1]),
                itemService.getAllItems(1L, 0, 1)
        );

        when(mockCommentRepository.findCommentsByItem_Id(1L))
                .thenReturn(List.of(testComments[0], testComments[1]));
        when(mockCommentRepository.findCommentsByItem_Id(2L))
                .thenReturn(List.of(testComments[2], testComments[3]));
        testItemFullResponseDtos[0].setComments(List.of(testCommentResponseDtos[0], testCommentResponseDtos[1]));
        testItemFullResponseDtos[1].setComments(List.of(testCommentResponseDtos[2], testCommentResponseDtos[3]));
        assertEquals(
                List.of(testItemFullResponseDtos[0], testItemFullResponseDtos[1]),
                itemService.getAllItems(1L, 0, 1)
        );

        when(mockBookingRepository.findLastBookingByItemId(1L))
                .thenReturn(testBookings[0]);
        when(mockBookingRepository.findNextBookingByItemId(1L))
                .thenReturn(testBookings[0]);
        when(mockBookingRepository.findLastBookingByItemId(2L))
                .thenReturn(testBookings[1]);
        when(mockBookingRepository.findNextBookingByItemId(2L))
                .thenReturn(testBookings[1]);
        testItemFullResponseDtos[0].setLastBooking(testBookingShortResponseDtos[0]);
        testItemFullResponseDtos[0].setNextBooking(testBookingShortResponseDtos[0]);
        testItemFullResponseDtos[1].setLastBooking(testBookingShortResponseDtos[1]);
        testItemFullResponseDtos[1].setNextBooking(testBookingShortResponseDtos[1]);
        assertEquals(
                List.of(testItemFullResponseDtos[0], testItemFullResponseDtos[1]),
                itemService.getAllItems(1L, 0, 1)
        );
        testItemFullResponseDtos[0].setComments(null);
        testItemFullResponseDtos[0].setLastBooking(null);
        testItemFullResponseDtos[0].setNextBooking(null);
        testItemFullResponseDtos[1].setComments(null);
        testItemFullResponseDtos[1].setLastBooking(null);
        testItemFullResponseDtos[1].setNextBooking(null);
    }

    @Test
    void testGetSearchedItems() {
        assertEquals(
                itemService.getSearchedItems("", 1, 2),
                Collections.emptyList()
        );

        when(mockItemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString(), any()))
                .thenReturn(List.of(testItems[0], testItems[1]));
        assertEquals(
                List.of(testItemResponseDtos[0], testItemResponseDtos[1]),
                itemService.getSearchedItems("test", 0, 1)
        );
    }

    @Test
    void testUpdateItem() {
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.updateItem(1L, testItemRequestDtos[0], 1L)
        );
        assertEquals(
                "Item id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(testItems[0]));
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        testItems[0].setOwner(null);
        exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.updateItem(1L, testItemRequestDtos[0], 1L)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );
        testItems[0].setOwner(testUser);

        exception = assertThrows(
                ForbiddenActionException.class,
                () -> itemService.updateItem(1L, testItemRequestDtos[0], 2L)
        );
        assertEquals(
                "Changing owner is forbidden.",
                exception.getMessage()
        );

        assertEquals(
                testItemResponseDtos[0],
                itemService.updateItem(
                        1L, new ItemRequestDto(null, null, null, 1L), 1L
                )
        );
        assertEquals(
                testItemResponseDtos[0],
                itemService.updateItem(
                        1L, new ItemRequestDto("", "", null, 1L), 1L
                )
        );
        assertEquals(
                new ItemResponseDto(
                        1L, "newName", "newDescription", false, 1L, null
                ),
                itemService.updateItem(
                        1L,
                        new ItemRequestDto("newName", "newDescription", false, 1L),
                        1L
                )
        );
        testItems[0].setName("ItemName1");
        testItems[0].setDescription("ItemDescription1");
        testItems[0].setAvailable(true);
    }

    @Test
    void testDeleteItem() {
        when(mockItemRepository.existsById(anyLong()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.deleteItem(1L)
        );
        assertEquals(
                "Item id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockItemRepository.existsById(anyLong()))
                .thenReturn(true);
        assertDoesNotThrow(() -> itemService.deleteItem(1L));
    }


    private static void initTestItem() {
        testItemRequestDtos = new ItemRequestDto[2];
        testItemRequestDtos[0] = new ItemRequestDto(
                "ItemName1", "ItemDescription1", true, 1L
        );
        testItemRequestDtos[1] = new ItemRequestDto(
                "ItemName2", "ItemDescription2", true, 1L
        );

        testItems = new Item[2];
        testItems[0] = new Item(
                1L, "ItemName1", "ItemDescription1", true, testUser, testItemRequest
        );
        testItems[1] = new Item(
                2L, "ItemName2", "ItemDescription2", true, testUser, testItemRequest
        );

        testItemResponseDtos = new ItemResponseDto[2];
        testItemResponseDtos[0] = new ItemResponseDto(
                1L, "ItemName1", "ItemDescription1", true, 1L, null
        );
        testItemResponseDtos[1] = new ItemResponseDto(
                2L, "ItemName2", "ItemDescription2", true, 1L, null
        );

        testItemFullResponseDtos = new ItemFullResponseDto[2];
        testItemFullResponseDtos[0] = new ItemFullResponseDto(
                1L,
                "ItemName1",
                "ItemDescription1",
                true,
                1L,
                null,
                null,
                null
        );
        testItemFullResponseDtos[1] = new ItemFullResponseDto(
                2L,
                "ItemName2",
                "ItemDescription2",
                true,
                1L,
                null,
                null,
                null
        );
    }

    private static void initTestComment() {
        testCommentRequestDtos = new CommentRequestDto("CommentText1");

        testComments = new Comment[4];
        testComments[0] = new Comment(1L, "CommentText1", testItems[0], testUser, testLocalDateTime);
        testComments[1] = new Comment(2L, "CommentText2", testItems[0], testUser, testLocalDateTime);
        testComments[2] = new Comment(3L, "CommentText3", testItems[1], testUser, testLocalDateTime);
        testComments[3] = new Comment(4L, "CommentText4", testItems[1], testUser, testLocalDateTime);

        testCommentResponseDtos = new CommentResponseDto[4];
        testCommentResponseDtos[0] = new CommentResponseDto(
                1L, "CommentText1", "UserName1", testLocalDateTime
        );
        testCommentResponseDtos[1] = new CommentResponseDto(
                2L, "CommentText2", "UserName1", testLocalDateTime
        );
        testCommentResponseDtos[2] = new CommentResponseDto(
                3L, "CommentText3", "UserName1", testLocalDateTime
        );
        testCommentResponseDtos[3] = new CommentResponseDto(
                4L, "CommentText4", "UserName1", testLocalDateTime
        );
    }

    private static void initTestBooking() {
        testBookings = new Booking[2];
        testBookings[0] = new Booking(
                1L, testLocalDateTime, testLocalDateTime, BookingStatus.WAITING, testUser, testItems[0]
        );
        testBookings[1] = new Booking(
                2L, testLocalDateTime, testLocalDateTime, BookingStatus.WAITING, testUser, testItems[1]
        );

        testBookingShortResponseDtos = new BookingShortResponseDto[2];
        testBookingShortResponseDtos[0] = new BookingShortResponseDto(1L, 1L);
        testBookingShortResponseDtos[1] = new BookingShortResponseDto(2L, 1L);
    }
}
