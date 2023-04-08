package ru.practicum.shareit.item;

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

    private static ItemRequestDto[] testItemRequestDto;
    private static Item[] testItem;
    private static ItemResponseDto[] testItemResponseDto;
    private static ItemFullResponseDto[] testItemFullResponseDto;

    private static CommentRequestDto testCommentRequestDto;
    private static Comment[] testComment;
    private static CommentResponseDto[] testCommentResponseDto;

    private static Booking[] testBooking;
    private static BookingShortResponseDto[] testBookingShortResponseDto;


    @BeforeAll
    static void beforeAll() {
        testLocalDateTime = LocalDateTime.of(2001, 1, 1, 1, 1);
        testUser = new User(1, "UserName1", "UserEmail1");
        testItemRequest = new ItemRequest(
                1, "ItemDescription1", testLocalDateTime, testUser, null
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
        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.addItem(testItemRequestDto[0], 1)
        );
        assertEquals(
                exception.getMessage(),
                "User id = 1 doesn't exist."
        );

        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.of(testUser));
        when(mockItemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.addItem(testItemRequestDto[0], 1)
        );
        assertEquals(
                exception.getMessage(),
                "Item Request id = 1 doesn't exist."
        );

        when(mockItemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.of(testItemRequest));
        when(mockItemRepository.save(any()))
                .thenReturn(testItem[0]);
        assertDoesNotThrow(() -> itemService.addItem(testItemRequestDto[0], 1));
    }

    @Test
    void testAddComment() {
        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.addComment(testCommentRequestDto, 1, 1)
        );
        assertEquals(
                exception.getMessage(),
                "User id = 1 doesn't exist."
        );

        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.of(testUser));
        when(mockItemRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.addComment(testCommentRequestDto, 1, 1)
        );
        assertEquals(
                exception.getMessage(),
                "Item id = 1 doesn't exist."
        );

        when(mockItemRepository.findById(anyInt()))
                .thenReturn(Optional.of(testItem[0]));
        exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.addComment(testCommentRequestDto, 1, 1)
        );
        assertEquals(
                exception.getMessage(),
                "Owner of the item can't leave comments on his own items."
        );

        when(mockBookingRepository.findLastBookingByItemIdAndBookerId(anyInt(), anyInt()))
                .thenReturn(null);
        exception = assertThrows(
                ValidationException.class,
                () -> itemService.addComment(testCommentRequestDto, 1, 2)
        );
        assertEquals(
                exception.getMessage(),
                "You must have this item in the past in order to leave a comment."
        );

        when(mockBookingRepository.findLastBookingByItemIdAndBookerId(anyInt(), anyInt()))
                .thenReturn(testBooking[0]);
        when(mockCommentRepository.save(any()))
                .thenReturn(testComment[0]);
        assertDoesNotThrow(() -> itemService.addComment(testCommentRequestDto, 1, 2));
    }

    @Test
    void testGetItem() {
        when(mockItemRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.getItem(1, 1)
        );
        assertEquals(
                exception.getMessage(),
                "Item id = 1 doesn't exist."
        );

        when(mockItemRepository.findById(anyInt()))
                .thenReturn(Optional.of(testItem[0]));
        when(mockCommentRepository.findCommentsByItem_Id(anyInt()))
                .thenReturn(List.of(testComment[0], testComment[1]));
        testItemFullResponseDto[0].setComments(List.of(testCommentResponseDto[0], testCommentResponseDto[1]));
        assertEquals(
                itemService.getItem(1, 2),
                testItemFullResponseDto[0]
        );

        when(mockBookingRepository.findLastBookingByItemId(anyInt()))
                .thenReturn(null);
        when(mockBookingRepository.findLastBookingByItemId(anyInt()))
                .thenReturn(null);
        assertEquals(
                itemService.getItem(1, 1),
                testItemFullResponseDto[0]
        );

        when(mockBookingRepository.findLastBookingByItemId(anyInt()))
                .thenReturn(testBooking[0]);
        when(mockBookingRepository.findNextBookingByItemId(anyInt()))
                .thenReturn(testBooking[1]);
        testItemFullResponseDto[0].setLastBooking(testBookingShortResponseDto[0]);
        testItemFullResponseDto[0].setNextBooking(testBookingShortResponseDto[1]);
        assertEquals(
                itemService.getItem(1, 1),
                testItemFullResponseDto[0]
        );
        testItemFullResponseDto[0].setLastBooking(null);
        testItemFullResponseDto[0].setNextBooking(null);
    }

    @Test
    void testGetAllItems() {
        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.getAllItems(1, 1, 1)
        );
        assertEquals(
                exception.getMessage(),
                "User id = 1 doesn't exist."
        );

        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(true);
        when(mockItemRepository.findByOwner_IdOrderById(anyInt()))
                .thenReturn(List.of(testItem[0], testItem[1], testItem[2], testItem[3]));
        testItemFullResponseDto[1].setComments(Collections.emptyList());
        testItemFullResponseDto[2].setComments(Collections.emptyList());
        assertEquals(
                itemService.getAllItems(1, 1, 2),
                List.of(testItemFullResponseDto[1], testItemFullResponseDto[2])
        );

        when(mockCommentRepository.findAll())
                .thenReturn(List.of(testComment[0], testComment[1], testComment[2], testComment[3]));
        testItemFullResponseDto[1].setComments(List.of(testCommentResponseDto[0], testCommentResponseDto[1]));
        testItemFullResponseDto[2].setComments(List.of(testCommentResponseDto[2], testCommentResponseDto[3]));
        assertEquals(
                itemService.getAllItems(1, 1, 2),
                List.of(testItemFullResponseDto[1], testItemFullResponseDto[2])
        );

        when(mockBookingRepository.findLastBookings())
                .thenReturn(List.of(testBooking[0], testBooking[1]));
        when(mockBookingRepository.findNextBookings())
                .thenReturn(List.of(testBooking[0], testBooking[1]));
        testItemFullResponseDto[1].setLastBooking(testBookingShortResponseDto[0]);
        testItemFullResponseDto[1].setNextBooking(testBookingShortResponseDto[0]);
        testItemFullResponseDto[2].setLastBooking(testBookingShortResponseDto[1]);
        testItemFullResponseDto[2].setNextBooking(testBookingShortResponseDto[1]);
        assertEquals(
                itemService.getAllItems(1, 1, 2),
                List.of(testItemFullResponseDto[1], testItemFullResponseDto[2])
        );
        testItemFullResponseDto[1].setComments(null);
        testItemFullResponseDto[1].setLastBooking(null);
        testItemFullResponseDto[1].setNextBooking(null);
        testItemFullResponseDto[2].setComments(null);
        testItemFullResponseDto[2].setLastBooking(null);
        testItemFullResponseDto[2].setNextBooking(null);
    }

    @Test
    void testGetSearchedItems() {
        assertEquals(
                itemService.getSearchedItems("", 1, 2),
                Collections.emptyList()
        );

        when(mockItemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString()))
                .thenReturn(List.of(testItem[0], testItem[1], testItem[2], testItem[3]));
        assertEquals(
                itemService.getSearchedItems("test", 1, 2),
                List.of(testItemResponseDto[1], testItemResponseDto[2])
        );
    }

    @Test
    void testUpdateItem() {
        when(mockItemRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.updateItem(1, testItemRequestDto[0], 1)
        );
        assertEquals(
                exception.getMessage(),
                "Item id = 1 doesn't exist."
        );

        when(mockItemRepository.findById(anyInt()))
                .thenReturn(Optional.of(testItem[0]));
        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        testItem[0].setOwner(null);
        exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.updateItem(1, testItemRequestDto[0], 1)
        );
        assertEquals(
                exception.getMessage(),
                "User id = 1 doesn't exist."
        );

        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.of(testUser));
        testItemRequestDto[1].setName(null);
        testItemRequestDto[1].setDescription(null);
        testItemRequestDto[1].setAvailable(null);
        assertEquals(
                itemService.updateItem(1, testItemRequestDto[1], 1),
                testItemResponseDto[0]
        );

        testItem[0].setOwner(null);
        testItemRequestDto[1].setName("");
        testItemRequestDto[1].setDescription("");
        assertEquals(
                itemService.updateItem(1, testItemRequestDto[1], 1),
                testItemResponseDto[0]
        );

        testItem[0].setOwner(testUser);
        exception = assertThrows(
                ForbiddenActionException.class,
                () -> itemService.updateItem(1, testItemRequestDto[1], 2)
        );
        assertEquals(
                exception.getMessage(),
                "Changing owner is forbidden."
        );

        testItemRequestDto[1].setName("ItemName2");
        testItemRequestDto[1].setDescription("ItemDescription2");
        testItemRequestDto[1].setAvailable(false);
        testItemResponseDto[1].setId(1);
        assertEquals(
                itemService.updateItem(1, testItemRequestDto[1], 1),
                testItemResponseDto[1]
        );
        testItemResponseDto[1].setId(2);
        testItem[0].setName("ItemName1");
        testItem[0].setDescription("ItemDescription1");
        testItem[0].setAvailable(true);
    }

    @Test
    void testDeleteItem() {
        when(mockItemRepository.existsById(anyInt()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemService.deleteItem(1)
        );
        assertEquals(
                exception.getMessage(),
                "Item id = 1 doesn't exist."
        );

        when(mockItemRepository.existsById(anyInt()))
                .thenReturn(true);
        assertDoesNotThrow(() -> itemService.deleteItem(1));
    }


    private static void initTestItem() {
        testItemRequestDto = new ItemRequestDto[2];
        testItemRequestDto[0] = new ItemRequestDto(
                "ItemName1", "ItemDescription1", true, 1
        );
        testItemRequestDto[1] = new ItemRequestDto(
                "ItemName2", "ItemDescription2", false, 1
        );

        testItem = new Item[4];
        testItem[0] = new Item(
                1, "ItemName1", "ItemDescription1", true, testUser, testItemRequest
        );
        testItem[1] = new Item(
                2, "ItemName2", "ItemDescription2", false, testUser, testItemRequest
        );
        testItem[2] = new Item(
                3, "ItemName3", "ItemDescription3", true, testUser, testItemRequest
        );
        testItem[3] = new Item(
                4, "ItemName4", "ItemDescription4", true, testUser, testItemRequest
        );

        testItemResponseDto = new ItemResponseDto[3];
        testItemResponseDto[0] = new ItemResponseDto(
                1, "ItemName1", "ItemDescription1", true, 1, null
        );
        testItemResponseDto[1] = new ItemResponseDto(
                2, "ItemName2", "ItemDescription2", false, 1, null
        );
        testItemResponseDto[2] = new ItemResponseDto(
                3, "ItemName3", "ItemDescription3", true, 1, null
        );

        testItemFullResponseDto = new ItemFullResponseDto[3];
        testItemFullResponseDto[0] = new ItemFullResponseDto(
                1,
                "ItemName1",
                "ItemDescription1",
                true,
                1,
                null,
                null,
                null
        );
        testItemFullResponseDto[1] = new ItemFullResponseDto(
                2,
                "ItemName2",
                "ItemDescription2",
                false,
                1,
                null,
                null,
                null
        );
        testItemFullResponseDto[2] = new ItemFullResponseDto(
                3,
                "ItemName3",
                "ItemDescription3",
                true,
                1,
                null,
                null,
                null
        );
    }

    private static void initTestComment() {
        testCommentRequestDto = new CommentRequestDto("CommentText1");

        testComment = new Comment[4];
        testComment[0] = new Comment(1, "CommentText1", testItem[1], testUser, testLocalDateTime);
        testComment[1] = new Comment(2, "CommentText2", testItem[1], testUser, testLocalDateTime);
        testComment[2] = new Comment(3, "CommentText3", testItem[2], testUser, testLocalDateTime);
        testComment[3] = new Comment(4, "CommentText4", testItem[2], testUser, testLocalDateTime);

        testCommentResponseDto = new CommentResponseDto[4];
        testCommentResponseDto[0] = new CommentResponseDto(
                1, "CommentText1", "UserName1", testLocalDateTime
        );
        testCommentResponseDto[1] = new CommentResponseDto(
                2, "CommentText2", "UserName1", testLocalDateTime
        );
        testCommentResponseDto[2] = new CommentResponseDto(
                3, "CommentText3", "UserName1", testLocalDateTime
        );
        testCommentResponseDto[3] = new CommentResponseDto(
                4, "CommentText4", "UserName1", testLocalDateTime
        );
    }

    private static void initTestBooking() {
        testBooking = new Booking[2];
        testBooking[0] = new Booking(
                1, testLocalDateTime, testLocalDateTime, BookingStatus.WAITING, testUser, testItem[1]
        );
        testBooking[1] = new Booking(
                2, testLocalDateTime, testLocalDateTime, BookingStatus.WAITING, testUser, testItem[2]
        );

        testBookingShortResponseDto = new BookingShortResponseDto[2];
        testBookingShortResponseDto[0] = new BookingShortResponseDto(1, 1);
        testBookingShortResponseDto[1] = new BookingShortResponseDto(2, 1);
    }
}
