package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.item.ItemFullResponseDto;
import ru.practicum.shareit.item.dto.item.ItemRequestDto;
import ru.practicum.shareit.item.dto.item.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ItemEndpointsTest {
    @Mock
    private ItemService mockItemService;
    @InjectMocks
    private ItemController itemController;
    private static ObjectMapper objectMapper;
    private MockMvc mockMvc;


    private static LocalDateTime testLocalDateTime;

    private static CommentRequestDto testCommentRequestDto;
    private static CommentResponseDto[] testCommentResponseDtos;

    private static ItemRequestDto testItemRequestDto;
    private static ItemResponseDto[] testItemResponseDtos;
    private static ItemFullResponseDto[] testItemFullResponseDtos;


    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testLocalDateTime = LocalDateTime.now();

        initTestComments();
        initTestItems();
    }

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }


    @Test
    void testAddItem() throws Exception {
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(
                                new ItemRequestDto(
                                        null, "ItemDescription1", false, 1
                                )
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(
                                new ItemRequestDto(
                                        "ItemName1", null, false, 1
                                )
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(
                                new ItemRequestDto(
                                        "ItemName1", "ItemDescription1", null, 1
                                )
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        when(mockItemService.addItem(any(), anyInt()))
                .thenReturn(testItemResponseDtos[0]);
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(testItemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemResponseDtos[0].getId())))
                .andExpect(jsonPath("$.name", is(testItemResponseDtos[0].getName())))
                .andExpect(jsonPath("$.description", is(testItemResponseDtos[0].getDescription())))
                .andExpect(jsonPath("$.available", is(testItemResponseDtos[0].getAvailable())))
                .andExpect(jsonPath("$.requestId", is(testItemResponseDtos[0].getRequestId())))
                .andExpect(jsonPath("$.comments", is(testItemResponseDtos[0].getComments())));
    }

    @Test
    void testAddComment() throws Exception {
        mockMvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(new CommentRequestDto(null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        when(mockItemService.addComment(any(), anyInt(), anyInt()))
                .thenReturn(testCommentResponseDtos[0]);
        mockMvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(testCommentRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testCommentResponseDtos[0].getId())))
                .andExpect(jsonPath("$.text", is(testCommentResponseDtos[0].getText())))
                .andExpect(jsonPath("$.authorName", is(testCommentResponseDtos[0].getAuthorName())))
                .andExpect(jsonPath("$.created[0]", is(testCommentResponseDtos[0].getCreated().getYear())));
    }

    @Test
    void testGetItem() throws Exception {
        when(mockItemService.getItem(anyInt(), anyInt()))
                .thenReturn(testItemFullResponseDtos[0]);
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemFullResponseDtos[0].getId())))
                .andExpect(jsonPath("$.name", is(testItemFullResponseDtos[0].getName())))
                .andExpect(jsonPath("$.description", is(testItemFullResponseDtos[0].getDescription())))
                .andExpect(jsonPath("$.available", is(testItemFullResponseDtos[0].getAvailable())))
                .andExpect(jsonPath("$.requestId", is(testItemFullResponseDtos[0].getRequestId())))
                .andExpect(jsonPath("$.lastBooking.id", is(testItemFullResponseDtos[0].getLastBooking().getId())))
                .andExpect(jsonPath("$.nextBooking.id", is(testItemFullResponseDtos[0].getNextBooking().getId())))
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.comments[0].id", is(testItemFullResponseDtos[0].getComments().get(0).getId())))
                .andExpect(jsonPath("$.comments[1].id", is(testItemFullResponseDtos[0].getComments().get(1).getId())));
    }

    @Test
    void testGetAllItems() throws Exception {
        when(mockItemService.getAllItems(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(testItemFullResponseDtos[0], testItemFullResponseDtos[1]));
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        mockMvc.perform(get("/items?from=1&size=2")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testItemFullResponseDtos[0].getId())))
                .andExpect(jsonPath("$[0].name", is(testItemFullResponseDtos[0].getName())))
                .andExpect(jsonPath("$[0].description", is(testItemFullResponseDtos[0].getDescription())))
                .andExpect(jsonPath("$[0].available", is(testItemFullResponseDtos[0].getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(testItemFullResponseDtos[0].getRequestId())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(testItemFullResponseDtos[0].getLastBooking().getId())))
                .andExpect(jsonPath("$[0].nextBooking.id", is(testItemFullResponseDtos[0].getNextBooking().getId())))
                .andExpect(jsonPath("$[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[0].comments[0].id", is(testItemFullResponseDtos[0].getComments().get(0).getId())))
                .andExpect(jsonPath("$[0].comments[1].id", is(testItemFullResponseDtos[0].getComments().get(1).getId())))
                .andExpect(jsonPath("$[1].id", is(testItemFullResponseDtos[1].getId())))
                .andExpect(jsonPath("$[1].name", is(testItemFullResponseDtos[1].getName())))
                .andExpect(jsonPath("$[1].description", is(testItemFullResponseDtos[1].getDescription())))
                .andExpect(jsonPath("$[1].available", is(testItemFullResponseDtos[1].getAvailable())))
                .andExpect(jsonPath("$[1].requestId", is(testItemFullResponseDtos[1].getRequestId())))
                .andExpect(jsonPath("$[1].lastBooking.id", is(testItemFullResponseDtos[1].getLastBooking().getId())))
                .andExpect(jsonPath("$[1].nextBooking.id", is(testItemFullResponseDtos[1].getNextBooking().getId())))
                .andExpect(jsonPath("$[1].comments", hasSize(2)))
                .andExpect(jsonPath("$[1].comments[0].id", is(testItemFullResponseDtos[1].getComments().get(0).getId())))
                .andExpect(jsonPath("$[1].comments[1].id", is(testItemFullResponseDtos[1].getComments().get(1).getId())));
    }

    @Test
    void testGetSearchedItems() throws Exception {
        when(mockItemService.getSearchedItems(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(testItemResponseDtos[1], testItemResponseDtos[2]));
        mockMvc.perform(get("/items/search?text=test"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/items/search?text=test&from=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testItemResponseDtos[1].getId())))
                .andExpect(jsonPath("$[0].name", is(testItemResponseDtos[1].getName())))
                .andExpect(jsonPath("$[0].description", is(testItemResponseDtos[1].getDescription())))
                .andExpect(jsonPath("$[0].available", is(testItemResponseDtos[1].getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(testItemResponseDtos[1].getRequestId())))
                .andExpect(jsonPath("$[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[0].comments[0].id", is(testItemResponseDtos[1].getComments().get(0).getId())))
                .andExpect(jsonPath("$[0].comments[1].id", is(testItemResponseDtos[1].getComments().get(1).getId())))
                .andExpect(jsonPath("$[1].id", is(testItemResponseDtos[2].getId())))
                .andExpect(jsonPath("$[1].name", is(testItemResponseDtos[2].getName())))
                .andExpect(jsonPath("$[1].description", is(testItemResponseDtos[2].getDescription())))
                .andExpect(jsonPath("$[1].available", is(testItemResponseDtos[2].getAvailable())))
                .andExpect(jsonPath("$[1].requestId", is(testItemResponseDtos[2].getRequestId())))
                .andExpect(jsonPath("$[1].comments", hasSize(2)))
                .andExpect(jsonPath("$[1].comments[0].id", is(testItemResponseDtos[2].getComments().get(0).getId())))
                .andExpect(jsonPath("$[1].comments[1].id", is(testItemResponseDtos[2].getComments().get(1).getId())));
    }

    @Test
    void testUpdateItem() throws Exception {
        when(mockItemService.updateItem(anyInt(), any(), anyInt()))
                .thenReturn(testItemResponseDtos[1]);
        mockMvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(testItemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemResponseDtos[1].getId())))
                .andExpect(jsonPath("$.name", is(testItemResponseDtos[1].getName())))
                .andExpect(jsonPath("$.description", is(testItemResponseDtos[1].getDescription())))
                .andExpect(jsonPath("$.available", is(testItemResponseDtos[1].getAvailable())))
                .andExpect(jsonPath("$.requestId", is(testItemResponseDtos[1].getRequestId())))
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.comments[0].id", is(testItemResponseDtos[1].getComments().get(0).getId())))
                .andExpect(jsonPath("$.comments[1].id", is(testItemResponseDtos[1].getComments().get(1).getId())));
    }

    @Test
    void testDeleteItem() throws Exception {
        doNothing().when(mockItemService).deleteItem(anyInt());
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isOk());
    }

    private static void initTestComments() {
        testCommentRequestDto = new CommentRequestDto("CommentText1");

        testCommentResponseDtos = new CommentResponseDto[2];
        testCommentResponseDtos[0] = new CommentResponseDto(
                1, "CommentText1", "UserName1", testLocalDateTime
        );
        testCommentResponseDtos[1] = new CommentResponseDto(
                2, "CommentText2", "UserName2", testLocalDateTime
        );
    }

    private static void initTestItems() {
        testItemRequestDto = new ItemRequestDto(
                "ItemName1", "ItemDescription1", false, null
        );

        testItemResponseDtos = new ItemResponseDto[3];
        testItemResponseDtos[0] = new ItemResponseDto(
                1, "ItemName1", "ItemDescription1", true, null, null
        );
        testItemResponseDtos[1] = new ItemResponseDto(
                2,
                "ItemName2",
                "ItemDescription3",
                false,
                1,
                List.of(testCommentResponseDtos[0], testCommentResponseDtos[1])
        );
        testItemResponseDtos[2] = new ItemResponseDto(
                3,
                "ItemName3",
                "ItemDescription3",
                true,
                3,
                List.of(testCommentResponseDtos[0], testCommentResponseDtos[1])
        );

        testItemFullResponseDtos = new ItemFullResponseDto[2];
        testItemFullResponseDtos[0] = new ItemFullResponseDto(
                1,
                "ItemName1",
                "ItemDescription1",
                false,
                1,
                new BookingShortResponseDto(1, 1),
                new BookingShortResponseDto(2, 2),
                List.of(testCommentResponseDtos[0], testCommentResponseDtos[1])
        );
        testItemFullResponseDtos[1] = new ItemFullResponseDto(
                2,
                "ItemName2",
                "ItemDescription2",
                true,
                2,
                new BookingShortResponseDto(3, 3),
                new BookingShortResponseDto(4, 4),
                List.of(testCommentResponseDtos[0], testCommentResponseDtos[1])
        );
    }
}
