package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.item.ItemFullResponseDto;
import ru.practicum.shareit.item.dto.item.ItemRequestDto;
import ru.practicum.shareit.item.dto.item.ItemResponseDto;
import ru.practicum.shareit.item.dto.item.ItemShortResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestFullResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserShortResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MapperTest {
    private static LocalDateTime testLocalDateTime;
    private static User testUser;
    private static ItemRequest testItemRequest;
    private static Item testItem;
    private static Comment testComment;
    private static Booking testBooking;


    @BeforeAll
    static void beforeAll() {
        testLocalDateTime = LocalDateTime.of(2001, 1, 1, 1, 1);
        testUser = new User(1L, "test1", "test1");
        testItemRequest = new ItemRequest(
                1L, "test1", testLocalDateTime, testUser, Collections.emptyList()
        );
        testItem = new Item(1L, "test1", "test1", true, testUser, testItemRequest);
        testComment = new Comment(1L, "test1", testItem, testUser, testLocalDateTime);
        testBooking = new Booking(
                1L, testLocalDateTime, testLocalDateTime, BookingStatus.WAITING, testUser, testItem
        );
    }


    @Test
    void testUserMapper() {
        assertEquals(
                new UserResponseDto(1L, "test1", "test1"),
                UserMapper.toUserDto(testUser)
        );

        assertEquals(
                new UserShortResponseDto(1L),
                UserMapper.toShortUserDto(testUser)
        );

        assertEquals(
                testUser,
                UserMapper.toUser(new UserRequestDto("test1", "test1"))
        );
    }

    @Test
    void testItemMapper() {
        testItem.setRequest(testItemRequest);
        assertEquals(
                new ItemFullResponseDto(1L, "test1", "test1", true, 1L),
                ItemMapper.toFullItemDto(testItem)
        );
        testItem.setRequest(null);
        assertEquals(
                new ItemFullResponseDto(1L, "test1", "test1", true, null),
                ItemMapper.toFullItemDto(testItem)
        );
        testItem.setRequest(testItemRequest);

        assertEquals(
                new ItemResponseDto(1L, "test1", "test1", true, 1L),
                ItemMapper.toItemDto(testItem)
        );
        testItem.setRequest(null);
        assertEquals(
                new ItemResponseDto(1L, "test1", "test1", true, null),
                ItemMapper.toItemDto(testItem)
        );
        testItem.setRequest(testItemRequest);

        assertEquals(
                new ItemShortResponseDto(1L, "test1"),
                ItemMapper.toShortItemDto(testItem)
        );

        assertEquals(
                new Item(1L, "test1", "test1", true, null, null),
                ItemMapper.toItem(new ItemRequestDto("test1", "test1", true, null))
        );
    }

    @Test
    void testBookingMapper() {
        assertEquals(
                new BookingResponseDto(
                        1L,
                        testLocalDateTime,
                        testLocalDateTime,
                        BookingStatus.WAITING,
                        UserMapper.toShortUserDto(testUser),
                        ItemMapper.toShortItemDto(testItem)
                ),
                BookingMapper.toBookingDto(testBooking)
        );

        assertEquals(
                new Booking(testLocalDateTime, testLocalDateTime, testUser, testItem),
                BookingMapper.toBooking(
                        new BookingRequestDto(1L, testLocalDateTime, testLocalDateTime), testUser, testItem
                )
        );
    }

    @Test
    void testItemRequestMapper() {
        testItemRequest.setItems(List.of(testItem, testItem));
        assertEquals(
                new ItemRequestFullResponseDto(
                        1L,
                        "test1",
                        testLocalDateTime,
                        List.of(ItemMapper.toItemDto(testItem), ItemMapper.toItemDto(testItem))
                ),
                ItemRequestMapper.toFullItemRequestDto(testItemRequest)
        );
        testItemRequest.setItems(Collections.emptyList());

        assertEquals(
                new ItemRequestResponseDto(1L, "test1", testLocalDateTime),
                ItemRequestMapper.toItemRequestDto(testItemRequest)
        );

        assertEquals(
                new ItemRequest(1L, "test1", null, null, null),
                ItemRequestMapper.toItemRequest(new ItemRequestRequestDto("test1"))
        );
    }

    @Test
    void testCommentMapper() {
        assertEquals(
                new CommentResponseDto(1L, "test1", "test1", testLocalDateTime),
                CommentMapper.toCommentDto(testComment)
        );

        assertEquals(
                new Comment(1L, "test1", null, null, null),
                CommentMapper.toComment(new CommentRequestDto("test1"))
        );
    }
}
