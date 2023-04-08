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
        initTest();
    }

    @Test
    void testUserMapper() {
        assertEquals(
                UserMapper.toUserDto(testUser),
                new UserResponseDto(1, "test1", "test1")
        );

        assertEquals(
                UserMapper.toShortUserDto(testUser),
                new UserShortResponseDto(1)
        );

        assertEquals(
                UserMapper.toUser(new UserRequestDto("test1", "test1")),
                testUser
        );
    }

    @Test
    void testItemMapper() {
        testItem.setRequest(testItemRequest);
        assertEquals(
                ItemMapper.toFullItemDto(testItem),
                new ItemFullResponseDto(1, "test1", "test1", true, 1)
        );
        testItem.setRequest(null);
        assertEquals(
                ItemMapper.toFullItemDto(testItem),
                new ItemFullResponseDto(1, "test1", "test1", true, null)
        );
        testItem.setRequest(testItemRequest);

        assertEquals(
                ItemMapper.toItemDto(testItem),
                new ItemResponseDto(1, "test1", "test1", true, 1)
        );
        testItem.setRequest(null);
        assertEquals(
                ItemMapper.toItemDto(testItem),
                new ItemResponseDto(1, "test1", "test1", true, null)
        );
        testItem.setRequest(testItemRequest);

        assertEquals(
                ItemMapper.toShortItemDto(testItem),
                new ItemShortResponseDto(1, "test1")
        );

        System.out.println(ItemMapper.toItem(new ItemRequestDto("test1", "test1", true, null)).equals(testItem));
        assertEquals(
                ItemMapper.toItem(new ItemRequestDto("test1", "test1", true, null)),
                testItem
        );
    }

    @Test
    void testBookingMapper() {
        assertEquals(
                BookingMapper.toBookingDto(testBooking),
                new BookingResponseDto(
                        1,
                        testLocalDateTime,
                        testLocalDateTime,
                        BookingStatus.WAITING,
                        UserMapper.toShortUserDto(testUser),
                        ItemMapper.toShortItemDto(testItem)
                )
        );

        assertEquals(
                BookingMapper.toBooking(
                        new BookingRequestDto(1, testLocalDateTime, testLocalDateTime), testUser, testItem
                ),
                new Booking(testLocalDateTime, testLocalDateTime, testUser, testItem)
        );
    }

    @Test
    void testItemRequestMapper() {
        testItemRequest.setItems(List.of(testItem, testItem));
        assertEquals(
                ItemRequestMapper.toFullItemRequestDto(testItemRequest),
                new ItemRequestFullResponseDto(
                        1,
                        "test1",
                        testLocalDateTime,
                        List.of(ItemMapper.toItemDto(testItem), ItemMapper.toItemDto(testItem))
                )
        );
        testItemRequest.setItems(Collections.emptyList());

        assertEquals(
                ItemRequestMapper.toItemRequestDto(testItemRequest),
                new ItemRequestResponseDto(1, "test1", testLocalDateTime)
        );

        assertEquals(
                ItemRequestMapper.toItemRequest(new ItemRequestRequestDto("test1")),
                testItemRequest
        );
    }

    @Test
    void testCommentMapper() {
        assertEquals(
                CommentMapper.toCommentDto(testComment),
                new CommentResponseDto(1, "test1", "test1", testLocalDateTime)
        );

        assertEquals(
                CommentMapper.toComment(new CommentRequestDto("test1")),
                testComment
        );
    }

    private static void initTest() {
        testLocalDateTime = LocalDateTime.of(2001, 1, 1, 1, 1);

        testUser = new User(1, "test1", "test1");

        testItemRequest = new ItemRequest(
                1,
                "test1",
                testLocalDateTime,
                testUser,
                Collections.emptyList()
        );

        testItem = new Item(
                1,
                "test1",
                "test1",
                true,
                testUser,
                testItemRequest
        );

        testComment = new Comment(
                1,
                "test1",
                testItem,
                testUser,
                testLocalDateTime
        );

        testBooking = new Booking(
                1,
                testLocalDateTime,
                testLocalDateTime,
                BookingStatus.WAITING,
                testUser,
                testItem
        );
    }
}
