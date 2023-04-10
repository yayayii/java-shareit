package ru.practicum.shareit;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.item.ItemRequestDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@Rollback(false)
@AllArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "spring.datasource.url=jdbc:postgresql://localhost:5432/test")
public class IntegrationTest {
    private final EntityManager entityManager;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final ItemRequestService itemRequestService;


    private static LocalDateTime testLocalDateTime;
    private static UserRequestDto testUserRequestDto[];
    private static ItemRequestDto testItemRequestDto;
    private static BookingRequestDto testBookingRequestDto;
    private static ItemRequestRequestDto testItemRequestRequestDto;
    private static CommentRequestDto testCommentRequestDto;


    @BeforeAll
    static void beforeAll() {
        testLocalDateTime = LocalDateTime.of(2001, 1, 1, 1, 1);
        testUserRequestDto = new UserRequestDto[2];
        testUserRequestDto[0] = new UserRequestDto("UserName1", "UserEmail1");
        testUserRequestDto[1] = new UserRequestDto("UserName2", "UserEmail2");
        testItemRequestRequestDto = new ItemRequestRequestDto("ItemRequestDescription1");
        testItemRequestDto = new ItemRequestDto(
                "ItemName1", "ItemDescription1", true, null
        );
        testBookingRequestDto = new BookingRequestDto(1, testLocalDateTime, testLocalDateTime);
        testCommentRequestDto = new CommentRequestDto("CommentText1");
    }

    @AfterEach
    void afterEach() {
        entityManager.createNativeQuery(
                            "delete from comment;" +
                            "delete from booking;" +
                            "delete from item;" +
                            "delete from request;" +
                            "delete from user_account;" +
                            "alter table comment" +
                            "    alter column id" +
                            "        restart with 1;" +
                            "alter table booking" +
                            "    alter column id" +
                            "        restart with 1;" +
                            "alter table item" +
                            "    alter column id" +
                            "        restart with 1;" +
                            "alter table request" +
                            "    alter column id" +
                            "        restart with 1;" +
                            "alter table user_account" +
                            "    alter column id" +
                            "        restart with 1;")
                .executeUpdate();
    }


    @Test
    void testSaveUser() {
        userService.addUser(testUserRequestDto[0]);
        User user = entityManager.createQuery("select u from User u where u.id = ?1", User.class)
                .setParameter(1, 1).getSingleResult();

        assertEquals(
                1,
                user.getId()
        );
        assertEquals(
                testUserRequestDto[0].getName(),
                user.getName()
        );
        assertEquals(
                testUserRequestDto[0].getEmail(),
                user.getEmail()
        );
    }

    @Test
    void testSaveItemRequest() {
        userService.addUser(testUserRequestDto[0]);
        itemRequestService.addItemRequest(testItemRequestRequestDto, 1);
        ItemRequest itemRequest = entityManager.createQuery(
                "select ir from ItemRequest ir where ir.id = ?1", ItemRequest.class
        ).setParameter(1, 1).getSingleResult();

        assertEquals(
                1,
                itemRequest.getId()
        );
        assertEquals(
                testItemRequestRequestDto.getDescription(),
                itemRequest.getDescription()
        );
        assertNotNull(itemRequest.getCreated());
        assertEquals(
                1,
                itemRequest.getRequester().getId()
        );
        assertEquals(
                null,
                itemRequest.getItems()
        );
    }

    @Test
    void testSaveItem() {
        userService.addUser(testUserRequestDto[0]);
        itemService.addItem(testItemRequestDto, 1);
        Item item = entityManager.createQuery("select i from Item i where i.id = ?1", Item.class)
                .setParameter(1, 1).getSingleResult();

        assertEquals(
                1,
                item.getId()
        );
        assertEquals(
                testItemRequestDto.getName(),
                item.getName()
        );
        assertEquals(
                testItemRequestDto.getDescription(),
                item.getDescription()
        );
        assertEquals(
                testItemRequestDto.getAvailable(),
                item.getAvailable()
        );
        assertEquals(
                1,
                item.getOwner().getId()
        );
        assertNull(item.getRequest());
    }

    @Test
    void testSaveBooking() {
        userService.addUser(testUserRequestDto[0]);
        userService.addUser(testUserRequestDto[1]);
        itemService.addItem(testItemRequestDto, 1);
        bookingService.addBooking(testBookingRequestDto, 2);
        Booking booking = entityManager.createQuery("select b from Booking b where b.id = ?1", Booking.class)
                .setParameter(1, 1).getSingleResult();

        assertEquals(
                1,
                booking.getId()
        );
        assertEquals(
                testLocalDateTime,
                booking.getStart()
        );
        assertEquals(
                testLocalDateTime,
                booking.getEnd()
        );
        assertEquals(
                BookingStatus.WAITING,
                booking.getStatus()
        );
        assertEquals(
                2,
                booking.getBooker().getId()
        );
        assertEquals(
                testBookingRequestDto.getItemId(),
                booking.getItem().getId()
        );
    }

    @Test
    void testSaveComment() {
        userService.addUser(testUserRequestDto[0]);
        userService.addUser(testUserRequestDto[1]);
        itemService.addItem(testItemRequestDto, 1);
        bookingService.addBooking(testBookingRequestDto, 2);
        bookingService.updateBooking(1, 1, true);
        itemService.addComment(testCommentRequestDto, 1, 2);
        Comment comment = entityManager.createQuery("select c from Comment c where c.id = ?1", Comment.class)
                .setParameter(1, 1).getSingleResult();

        assertEquals(
                1,
                comment.getId()
        );
        assertEquals(
                testCommentRequestDto.getText(),
                comment.getText()
        );
        assertEquals(
                1,
                comment.getItem().getId()
        );
        assertEquals(
                2,
                comment.getAuthor().getId()
        );
        assertNotNull(comment.getCreated());
    }
}
