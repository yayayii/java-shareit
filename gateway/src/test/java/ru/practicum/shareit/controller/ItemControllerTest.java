package ru.practicum.shareit.controller;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.dto.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private ItemClient mockItemClient;
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
                                        null, "ItemDescription1", false, 1L
                                )
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(
                                new ItemRequestDto(
                                        "ItemName1", null, false, 1L
                                )
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(
                                new ItemRequestDto(
                                        "ItemName1", "ItemDescription1", null, 1L
                                )
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        when(mockItemClient.addItem(any(), anyLong()))
                .thenReturn(ResponseEntity.ok(testItemResponseDtos[0]));
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(testItemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testItemResponseDtos[0].getName())))
                .andExpect(jsonPath("$.description", is(testItemResponseDtos[0].getDescription())))
                .andExpect(jsonPath("$.available", is(testItemResponseDtos[0].getAvailable())))
                .andExpect(jsonPath("$.requestId", is(testItemResponseDtos[0].getRequestId()), Long.class))
                .andExpect(jsonPath("$.comments", is(testItemResponseDtos[0].getComments())));
    }

    @Test
    void testAddComment() throws Exception {
        mockMvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(new CommentRequestDto(null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        when(mockItemClient.addComment(any(), anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok(testCommentResponseDtos[0]));
        mockMvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(testCommentRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testCommentResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$.text", is(testCommentResponseDtos[0].getText())))
                .andExpect(jsonPath("$.authorName", is(testCommentResponseDtos[0].getAuthorName())))
                .andExpect(jsonPath("$.created[0]", is(testCommentResponseDtos[0].getCreated().getYear())));
    }

    @Test
    void testGetItem() throws Exception {
        when(mockItemClient.getItem(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok(testItemFullResponseDtos[0]));
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemFullResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testItemFullResponseDtos[0].getName())))
                .andExpect(jsonPath("$.description", is(testItemFullResponseDtos[0].getDescription())))
                .andExpect(jsonPath("$.available", is(testItemFullResponseDtos[0].getAvailable())))
                .andExpect(jsonPath("$.requestId", is(testItemFullResponseDtos[0].getRequestId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.id", is(testItemFullResponseDtos[0].getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(testItemFullResponseDtos[0].getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.comments[0].id", is(testItemFullResponseDtos[0].getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.comments[1].id", is(testItemFullResponseDtos[0].getComments().get(1).getId()), Long.class));
    }

    @Test
    void testGetAllItems() throws Exception {
        when(mockItemClient.getAllItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(List.of(testItemFullResponseDtos[0], testItemFullResponseDtos[1])));
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        mockMvc.perform(get("/items?from=1&size=2")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testItemFullResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(testItemFullResponseDtos[0].getName())))
                .andExpect(jsonPath("$[0].description", is(testItemFullResponseDtos[0].getDescription())))
                .andExpect(jsonPath("$[0].available", is(testItemFullResponseDtos[0].getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(testItemFullResponseDtos[0].getRequestId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.id", is(testItemFullResponseDtos[0].getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.id", is(testItemFullResponseDtos[0].getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[0].comments[0].id", is(testItemFullResponseDtos[0].getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].comments[1].id", is(testItemFullResponseDtos[0].getComments().get(1).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(testItemFullResponseDtos[1].getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(testItemFullResponseDtos[1].getName())))
                .andExpect(jsonPath("$[1].description", is(testItemFullResponseDtos[1].getDescription())))
                .andExpect(jsonPath("$[1].available", is(testItemFullResponseDtos[1].getAvailable())))
                .andExpect(jsonPath("$[1].requestId", is(testItemFullResponseDtos[1].getRequestId()), Long.class))
                .andExpect(jsonPath("$[1].lastBooking.id", is(testItemFullResponseDtos[1].getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[1].nextBooking.id", is(testItemFullResponseDtos[1].getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[1].comments", hasSize(2)))
                .andExpect(jsonPath("$[1].comments[0].id", is(testItemFullResponseDtos[1].getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[1].comments[1].id", is(testItemFullResponseDtos[1].getComments().get(1).getId()), Long.class));
    }

    @Test
    void testGetSearchedItems() throws Exception {
        when(mockItemClient.getSearchedItems(anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(List.of(testItemResponseDtos[1], testItemResponseDtos[2])));
        mockMvc.perform(get("/items/search?text=test"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/items/search?text=test&from=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testItemResponseDtos[1].getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(testItemResponseDtos[1].getName())))
                .andExpect(jsonPath("$[0].description", is(testItemResponseDtos[1].getDescription())))
                .andExpect(jsonPath("$[0].available", is(testItemResponseDtos[1].getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(testItemResponseDtos[1].getRequestId()), Long.class))
                .andExpect(jsonPath("$[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[0].comments[0].id", is(testItemResponseDtos[1].getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].comments[1].id", is(testItemResponseDtos[1].getComments().get(1).getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(testItemResponseDtos[2].getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(testItemResponseDtos[2].getName())))
                .andExpect(jsonPath("$[1].description", is(testItemResponseDtos[2].getDescription())))
                .andExpect(jsonPath("$[1].available", is(testItemResponseDtos[2].getAvailable())))
                .andExpect(jsonPath("$[1].requestId", is(testItemResponseDtos[2].getRequestId()), Long.class))
                .andExpect(jsonPath("$[1].comments", hasSize(2)))
                .andExpect(jsonPath("$[1].comments[0].id", is(testItemResponseDtos[2].getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$[1].comments[1].id", is(testItemResponseDtos[2].getComments().get(1).getId()), Long.class));
    }

    @Test
    void testUpdateItem() throws Exception {
        when(mockItemClient.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(ResponseEntity.ok(testItemResponseDtos[1]));
        mockMvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(testItemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemResponseDtos[1].getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testItemResponseDtos[1].getName())))
                .andExpect(jsonPath("$.description", is(testItemResponseDtos[1].getDescription())))
                .andExpect(jsonPath("$.available", is(testItemResponseDtos[1].getAvailable())))
                .andExpect(jsonPath("$.requestId", is(testItemResponseDtos[1].getRequestId()), Long.class))
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.comments[0].id", is(testItemResponseDtos[1].getComments().get(0).getId()), Long.class))
                .andExpect(jsonPath("$.comments[1].id", is(testItemResponseDtos[1].getComments().get(1).getId()), Long.class));
    }

    @Test
    void testDeleteItem() throws Exception {
        when(mockItemClient.deleteItem(anyLong())).thenReturn(ResponseEntity.ok(any()));
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isOk());
    }

    private static void initTestComments() {
        testCommentRequestDto = new CommentRequestDto("CommentText1");

        testCommentResponseDtos = new CommentResponseDto[2];
        testCommentResponseDtos[0] = new CommentResponseDto(
                1L, "CommentText1", "UserName1", testLocalDateTime
        );
        testCommentResponseDtos[1] = new CommentResponseDto(
                2L, "CommentText2", "UserName2", testLocalDateTime
        );
    }

    private static void initTestItems() {
        testItemRequestDto = new ItemRequestDto(
                "ItemName1", "ItemDescription1", false, null
        );

        testItemResponseDtos = new ItemResponseDto[3];
        testItemResponseDtos[0] = new ItemResponseDto(
                1L, "ItemName1", "ItemDescription1", true, null, null
        );
        testItemResponseDtos[1] = new ItemResponseDto(
                2L,
                "ItemName2",
                "ItemDescription3",
                false,
                1L,
                List.of(testCommentResponseDtos[0], testCommentResponseDtos[1])
        );
        testItemResponseDtos[2] = new ItemResponseDto(
                3L,
                "ItemName3",
                "ItemDescription3",
                true,
                3L,
                List.of(testCommentResponseDtos[0], testCommentResponseDtos[1])
        );

        testItemFullResponseDtos = new ItemFullResponseDto[2];
        testItemFullResponseDtos[0] = new ItemFullResponseDto(
                1L,
                "ItemName1",
                "ItemDescription1",
                false,
                1L,
                new BookingShortResponseDto(1L, 1L),
                new BookingShortResponseDto(2L, 2L),
                List.of(testCommentResponseDtos[0], testCommentResponseDtos[1])
        );
        testItemFullResponseDtos[1] = new ItemFullResponseDto(
                2L,
                "ItemName2",
                "ItemDescription2",
                true,
                2L,
                new BookingShortResponseDto(3L, 3L),
                new BookingShortResponseDto(4L, 4L),
                List.of(testCommentResponseDtos[0], testCommentResponseDtos[1])
        );
    }
}
