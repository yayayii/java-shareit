package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.dto.comment.CommentResponseDto;
import ru.practicum.shareit.item.dto.item.ItemResponseDto;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestFullResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ItemRequestEndpointsTest {
    @Mock
    private ItemRequestService mockItemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;
    private static ObjectMapper objectMapper;
    private MockMvc mockMvc;


    private static LocalDateTime testLocalDateTime;

    private static CommentResponseDto[] testCommentResponseDtos;

    private static ItemResponseDto[] testItemResponseDtos;

    private static ItemRequestRequestDto testItemRequestRequestDto;
    private static ItemRequestResponseDto testItemRequestResponseDto;
    private static ItemRequestFullResponseDto[] testItemRequestFullResponseDtos;


    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testLocalDateTime = LocalDateTime.now();

        initTestComments();
        initTestItems();
        initTestItemRequests();
    }

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
    }


    @Test
    void testAddItemRequest() throws Exception {
        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(new ItemRequestRequestDto(null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        when(mockItemRequestService.addItemRequest(any(), anyInt()))
                .thenReturn(testItemRequestResponseDto);
        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(testItemRequestRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemRequestResponseDto.getId())))
                .andExpect(jsonPath("$.description", is(testItemRequestResponseDto.getDescription())))
                .andExpect(jsonPath("$.created[0]", is(testItemRequestResponseDto.getCreated().getYear())));
    }

    @Test
    void testGetItemRequest() throws Exception {
        when(mockItemRequestService.getItemRequest(anyInt(), anyInt()))
                .thenReturn(testItemRequestFullResponseDtos[0]);
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testItemRequestFullResponseDtos[0].getId())))
                .andExpect(jsonPath("$.description", is(testItemRequestFullResponseDtos[0].getDescription())))
                .andExpect(jsonPath("$.created[0]", is(testItemRequestFullResponseDtos[0].getCreated().getYear())))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].id", is(testItemRequestFullResponseDtos[0].getItems().get(0).getId())))
                .andExpect(jsonPath("$.items[0].name", is(testItemRequestFullResponseDtos[0].getItems().get(0).getName())))
                .andExpect(jsonPath("$.items[0].description", is(testItemRequestFullResponseDtos[0].getItems().get(0).getDescription())))
                .andExpect(jsonPath("$.items[0].available", is(testItemRequestFullResponseDtos[0].getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$.items[0].requestId", is(testItemRequestFullResponseDtos[0].getItems().get(0).getRequestId())))
                .andExpect(jsonPath("$.items[0].comments", hasSize(2)))
                .andExpect(jsonPath("$.items[0].comments[0].id", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getId())))
                .andExpect(jsonPath("$.items[0].comments[0].text", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getText())))
                .andExpect(jsonPath("$.items[0].comments[0].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$.items[0].comments[0].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getCreated().getYear())))
                .andExpect(jsonPath("$.items[0].comments[1].id", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getId())))
                .andExpect(jsonPath("$.items[0].comments[1].text", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getText())))
                .andExpect(jsonPath("$.items[0].comments[1].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$.items[0].comments[1].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getCreated().getYear())))
                .andExpect(jsonPath("$.items[1].id", is(testItemRequestFullResponseDtos[0].getItems().get(1).getId())))
                .andExpect(jsonPath("$.items[1].name", is(testItemRequestFullResponseDtos[0].getItems().get(1).getName())))
                .andExpect(jsonPath("$.items[1].description", is(testItemRequestFullResponseDtos[0].getItems().get(1).getDescription())))
                .andExpect(jsonPath("$.items[1].available", is(testItemRequestFullResponseDtos[0].getItems().get(1).getAvailable())))
                .andExpect(jsonPath("$.items[1].requestId", is(testItemRequestFullResponseDtos[0].getItems().get(1).getRequestId())))
                .andExpect(jsonPath("$.items[1].comments", hasSize(2)))
                .andExpect(jsonPath("$.items[1].comments[0].id", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getId())))
                .andExpect(jsonPath("$.items[1].comments[0].text", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getText())))
                .andExpect(jsonPath("$.items[1].comments[0].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$.items[1].comments[0].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getCreated().getYear())))
                .andExpect(jsonPath("$.items[1].comments[1].id", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getId())))
                .andExpect(jsonPath("$.items[1].comments[1].text", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getText())))
                .andExpect(jsonPath("$.items[1].comments[1].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$.items[1].comments[1].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getCreated().getYear())));
    }

    @Test
    void testGetOwnItemRequest() throws Exception {
        when(mockItemRequestService.getOwnItemRequests(anyInt()))
                .thenReturn(List.of(testItemRequestFullResponseDtos[0], testItemRequestFullResponseDtos[1]));
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testItemRequestFullResponseDtos[0].getId())))
                .andExpect(jsonPath("$[0].description", is(testItemRequestFullResponseDtos[0].getDescription())))
                .andExpect(jsonPath("$[0].created[0]", is(testItemRequestFullResponseDtos[0].getCreated().getYear())))
                .andExpect(jsonPath("$[0].items", hasSize(2)))
                .andExpect(jsonPath("$[0].items[0].id", is(testItemRequestFullResponseDtos[0].getItems().get(0).getId())))
                .andExpect(jsonPath("$[0].items[0].name", is(testItemRequestFullResponseDtos[0].getItems().get(0).getName())))
                .andExpect(jsonPath("$[0].items[0].description", is(testItemRequestFullResponseDtos[0].getItems().get(0).getDescription())))
                .andExpect(jsonPath("$[0].items[0].available", is(testItemRequestFullResponseDtos[0].getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$[0].items[0].requestId", is(testItemRequestFullResponseDtos[0].getItems().get(0).getRequestId())))
                .andExpect(jsonPath("$[0].items[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[0].items[0].comments[0].id", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getId())))
                .andExpect(jsonPath("$[0].items[0].comments[0].text", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getText())))
                .andExpect(jsonPath("$[0].items[0].comments[0].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[0].items[0].comments[0].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getCreated().getYear())))
                .andExpect(jsonPath("$[0].items[0].comments[1].id", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getId())))
                .andExpect(jsonPath("$[0].items[0].comments[1].text", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getText())))
                .andExpect(jsonPath("$[0].items[0].comments[1].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$[0].items[0].comments[1].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getCreated().getYear())))
                .andExpect(jsonPath("$[0].items[1].id", is(testItemRequestFullResponseDtos[0].getItems().get(1).getId())))
                .andExpect(jsonPath("$[0].items[1].name", is(testItemRequestFullResponseDtos[0].getItems().get(1).getName())))
                .andExpect(jsonPath("$[0].items[1].description", is(testItemRequestFullResponseDtos[0].getItems().get(1).getDescription())))
                .andExpect(jsonPath("$[0].items[1].available", is(testItemRequestFullResponseDtos[0].getItems().get(1).getAvailable())))
                .andExpect(jsonPath("$[0].items[1].requestId", is(testItemRequestFullResponseDtos[0].getItems().get(1).getRequestId())))
                .andExpect(jsonPath("$[0].items[1].comments", hasSize(2)))
                .andExpect(jsonPath("$[0].items[1].comments[0].id", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getId())))
                .andExpect(jsonPath("$[0].items[1].comments[0].text", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getText())))
                .andExpect(jsonPath("$[0].items[1].comments[0].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[0].items[1].comments[0].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getCreated().getYear())))
                .andExpect(jsonPath("$[0].items[1].comments[1].id", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getId())))
                .andExpect(jsonPath("$[0].items[1].comments[1].text", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getText())))
                .andExpect(jsonPath("$[0].items[1].comments[1].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$[0].items[1].comments[1].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getCreated().getYear())))
                .andExpect(jsonPath("$[1].id", is(testItemRequestFullResponseDtos[1].getId())))
                .andExpect(jsonPath("$[1].description", is(testItemRequestFullResponseDtos[1].getDescription())))
                .andExpect(jsonPath("$[1].created[0]", is(testItemRequestFullResponseDtos[1].getCreated().getYear())))
                .andExpect(jsonPath("$[1].items", hasSize(2)))
                .andExpect(jsonPath("$[1].items[0].id", is(testItemRequestFullResponseDtos[1].getItems().get(0).getId())))
                .andExpect(jsonPath("$[1].items[0].name", is(testItemRequestFullResponseDtos[1].getItems().get(0).getName())))
                .andExpect(jsonPath("$[1].items[0].description", is(testItemRequestFullResponseDtos[1].getItems().get(0).getDescription())))
                .andExpect(jsonPath("$[1].items[0].available", is(testItemRequestFullResponseDtos[1].getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$[1].items[0].requestId", is(testItemRequestFullResponseDtos[1].getItems().get(0).getRequestId())))
                .andExpect(jsonPath("$[1].items[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[1].items[0].comments[0].id", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(0).getId())))
                .andExpect(jsonPath("$[1].items[0].comments[0].text", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(0).getText())))
                .andExpect(jsonPath("$[1].items[0].comments[0].authorName", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[1].items[0].comments[0].created[0]", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(0).getCreated().getYear())))
                .andExpect(jsonPath("$[1].items[0].comments[1].id", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(1).getId())))
                .andExpect(jsonPath("$[1].items[0].comments[1].text", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(1).getText())))
                .andExpect(jsonPath("$[1].items[0].comments[1].authorName", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$[1].items[0].comments[1].created[0]", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(1).getCreated().getYear())))
                .andExpect(jsonPath("$[1].items[1].id", is(testItemRequestFullResponseDtos[1].getItems().get(1).getId())))
                .andExpect(jsonPath("$[1].items[1].name", is(testItemRequestFullResponseDtos[1].getItems().get(1).getName())))
                .andExpect(jsonPath("$[1].items[1].description", is(testItemRequestFullResponseDtos[1].getItems().get(1).getDescription())))
                .andExpect(jsonPath("$[1].items[1].available", is(testItemRequestFullResponseDtos[1].getItems().get(1).getAvailable())))
                .andExpect(jsonPath("$[1].items[1].requestId", is(testItemRequestFullResponseDtos[1].getItems().get(1).getRequestId())))
                .andExpect(jsonPath("$[1].items[1].comments", hasSize(2)))
                .andExpect(jsonPath("$[1].items[1].comments[0].id", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(0).getId())))
                .andExpect(jsonPath("$[1].items[1].comments[0].text", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(0).getText())))
                .andExpect(jsonPath("$[1].items[1].comments[0].authorName", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[1].items[1].comments[0].created[0]", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(0).getCreated().getYear())))
                .andExpect(jsonPath("$[1].items[1].comments[1].id", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(1).getId())))
                .andExpect(jsonPath("$[1].items[1].comments[1].text", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(1).getText())))
                .andExpect(jsonPath("$[1].items[1].comments[1].authorName", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$[1].items[1].comments[1].created[0]", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(1).getCreated().getYear())));
    }

    @Test
    void testGetOtherItemRequest() throws Exception {
//        mockMvc.perform(get("/requests/all?from=-1")
//                        .header("X-Sharer-User-Id", 1))
//                .andExpect(status().isBadRequest());
//        mockMvc.perform(get("/requests/all?size=0")
//                        .header("X-Sharer-User-Id", 1))
//                .andExpect(status().isBadRequest());

        when(mockItemRequestService.getOtherItemRequests(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(testItemRequestFullResponseDtos[0], testItemRequestFullResponseDtos[1]));
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        mockMvc.perform(get("/requests/all?from=1&size=2")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testItemRequestFullResponseDtos[0].getId())))
                .andExpect(jsonPath("$[0].description", is(testItemRequestFullResponseDtos[0].getDescription())))
                .andExpect(jsonPath("$[0].created[0]", is(testItemRequestFullResponseDtos[0].getCreated().getYear())))
                .andExpect(jsonPath("$[0].items", hasSize(2)))
                .andExpect(jsonPath("$[0].items[0].id", is(testItemRequestFullResponseDtos[0].getItems().get(0).getId())))
                .andExpect(jsonPath("$[0].items[0].name", is(testItemRequestFullResponseDtos[0].getItems().get(0).getName())))
                .andExpect(jsonPath("$[0].items[0].description", is(testItemRequestFullResponseDtos[0].getItems().get(0).getDescription())))
                .andExpect(jsonPath("$[0].items[0].available", is(testItemRequestFullResponseDtos[0].getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$[0].items[0].requestId", is(testItemRequestFullResponseDtos[0].getItems().get(0).getRequestId())))
                .andExpect(jsonPath("$[0].items[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[0].items[0].comments[0].id", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getId())))
                .andExpect(jsonPath("$[0].items[0].comments[0].text", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getText())))
                .andExpect(jsonPath("$[0].items[0].comments[0].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[0].items[0].comments[0].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(0).getCreated().getYear())))
                .andExpect(jsonPath("$[0].items[0].comments[1].id", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getId())))
                .andExpect(jsonPath("$[0].items[0].comments[1].text", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getText())))
                .andExpect(jsonPath("$[0].items[0].comments[1].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$[0].items[0].comments[1].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(0).getComments().get(1).getCreated().getYear())))
                .andExpect(jsonPath("$[0].items[1].id", is(testItemRequestFullResponseDtos[0].getItems().get(1).getId())))
                .andExpect(jsonPath("$[0].items[1].name", is(testItemRequestFullResponseDtos[0].getItems().get(1).getName())))
                .andExpect(jsonPath("$[0].items[1].description", is(testItemRequestFullResponseDtos[0].getItems().get(1).getDescription())))
                .andExpect(jsonPath("$[0].items[1].available", is(testItemRequestFullResponseDtos[0].getItems().get(1).getAvailable())))
                .andExpect(jsonPath("$[0].items[1].requestId", is(testItemRequestFullResponseDtos[0].getItems().get(1).getRequestId())))
                .andExpect(jsonPath("$[0].items[1].comments", hasSize(2)))
                .andExpect(jsonPath("$[0].items[1].comments[0].id", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getId())))
                .andExpect(jsonPath("$[0].items[1].comments[0].text", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getText())))
                .andExpect(jsonPath("$[0].items[1].comments[0].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[0].items[1].comments[0].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(0).getCreated().getYear())))
                .andExpect(jsonPath("$[0].items[1].comments[1].id", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getId())))
                .andExpect(jsonPath("$[0].items[1].comments[1].text", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getText())))
                .andExpect(jsonPath("$[0].items[1].comments[1].authorName", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$[0].items[1].comments[1].created[0]", is(testItemRequestFullResponseDtos[0].getItems().get(1).getComments().get(1).getCreated().getYear())))
                .andExpect(jsonPath("$[1].id", is(testItemRequestFullResponseDtos[1].getId())))
                .andExpect(jsonPath("$[1].description", is(testItemRequestFullResponseDtos[1].getDescription())))
                .andExpect(jsonPath("$[1].created[0]", is(testItemRequestFullResponseDtos[1].getCreated().getYear())))
                .andExpect(jsonPath("$[1].items", hasSize(2)))
                .andExpect(jsonPath("$[1].items[0].id", is(testItemRequestFullResponseDtos[1].getItems().get(0).getId())))
                .andExpect(jsonPath("$[1].items[0].name", is(testItemRequestFullResponseDtos[1].getItems().get(0).getName())))
                .andExpect(jsonPath("$[1].items[0].description", is(testItemRequestFullResponseDtos[1].getItems().get(0).getDescription())))
                .andExpect(jsonPath("$[1].items[0].available", is(testItemRequestFullResponseDtos[1].getItems().get(0).getAvailable())))
                .andExpect(jsonPath("$[1].items[0].requestId", is(testItemRequestFullResponseDtos[1].getItems().get(0).getRequestId())))
                .andExpect(jsonPath("$[1].items[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[1].items[0].comments[0].id", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(0).getId())))
                .andExpect(jsonPath("$[1].items[0].comments[0].text", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(0).getText())))
                .andExpect(jsonPath("$[1].items[0].comments[0].authorName", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[1].items[0].comments[0].created[0]", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(0).getCreated().getYear())))
                .andExpect(jsonPath("$[1].items[0].comments[1].id", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(1).getId())))
                .andExpect(jsonPath("$[1].items[0].comments[1].text", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(1).getText())))
                .andExpect(jsonPath("$[1].items[0].comments[1].authorName", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$[1].items[0].comments[1].created[0]", is(testItemRequestFullResponseDtos[1].getItems().get(0).getComments().get(1).getCreated().getYear())))
                .andExpect(jsonPath("$[1].items[1].id", is(testItemRequestFullResponseDtos[1].getItems().get(1).getId())))
                .andExpect(jsonPath("$[1].items[1].name", is(testItemRequestFullResponseDtos[1].getItems().get(1).getName())))
                .andExpect(jsonPath("$[1].items[1].description", is(testItemRequestFullResponseDtos[1].getItems().get(1).getDescription())))
                .andExpect(jsonPath("$[1].items[1].available", is(testItemRequestFullResponseDtos[1].getItems().get(1).getAvailable())))
                .andExpect(jsonPath("$[1].items[1].requestId", is(testItemRequestFullResponseDtos[1].getItems().get(1).getRequestId())))
                .andExpect(jsonPath("$[1].items[1].comments", hasSize(2)))
                .andExpect(jsonPath("$[1].items[1].comments[0].id", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(0).getId())))
                .andExpect(jsonPath("$[1].items[1].comments[0].text", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(0).getText())))
                .andExpect(jsonPath("$[1].items[1].comments[0].authorName", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(0).getAuthorName())))
                .andExpect(jsonPath("$[1].items[1].comments[0].created[0]", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(0).getCreated().getYear())))
                .andExpect(jsonPath("$[1].items[1].comments[1].id", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(1).getId())))
                .andExpect(jsonPath("$[1].items[1].comments[1].text", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(1).getText())))
                .andExpect(jsonPath("$[1].items[1].comments[1].authorName", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(1).getAuthorName())))
                .andExpect(jsonPath("$[1].items[1].comments[1].created[0]", is(testItemRequestFullResponseDtos[1].getItems().get(1).getComments().get(1).getCreated().getYear())));
    }


    private static void initTestComments() {
        testCommentResponseDtos = new CommentResponseDto[2];
        testCommentResponseDtos[0] = new CommentResponseDto(
                1, "CommentText1", "UserName1", testLocalDateTime
        );
        testCommentResponseDtos[1] = new CommentResponseDto(
                2, "CommentText2", "UserName2", testLocalDateTime
        );
    }

    private static void initTestItems() {
        testItemResponseDtos = new ItemResponseDto[2];
        testItemResponseDtos[0] = new ItemResponseDto(
                1,
                "ItemName1",
                "ItemDescription1",
                true,
                1,
                List.of(testCommentResponseDtos[0], testCommentResponseDtos[1])
        );
        testItemResponseDtos[1] = new ItemResponseDto(
                2,
                "ItemName2",
                "ItemDescription2",
                false,
                2,
                List.of(testCommentResponseDtos[0], testCommentResponseDtos[1])
        );
    }

    private static void initTestItemRequests() {
        testItemRequestRequestDto = new ItemRequestRequestDto("ItemRequestDescription1");

        testItemRequestResponseDto = new ItemRequestResponseDto(
                1, "ItemRequestDescription1", testLocalDateTime
        );

        testItemRequestFullResponseDtos = new ItemRequestFullResponseDto[2];
        testItemRequestFullResponseDtos[0] = new ItemRequestFullResponseDto(
                1,
                "ItemRequestDescription1",
                testLocalDateTime,
                List.of(testItemResponseDtos[0], testItemResponseDtos[1])
        );
        testItemRequestFullResponseDtos[1] = new ItemRequestFullResponseDto(
                2,
                "ItemRequestDescription2",
                testLocalDateTime,
                List.of(testItemResponseDtos[0], testItemResponseDtos[1])
        );
    }
}
