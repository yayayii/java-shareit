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
import ru.practicum.shareit.client.BookingClient;
import ru.practicum.shareit.dto.request.BookingRequestDto;
import ru.practicum.shareit.dto.response.booking.BookingResponseDto;
import ru.practicum.shareit.dto.response.booking.BookingStatus;
import ru.practicum.shareit.dto.response.item.ItemShortResponseDto;
import ru.practicum.shareit.dto.response.user.UserShortResponseDto;

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
public class BookingControllerTest {
    @Mock
    private BookingClient mockBookingClient;
    @InjectMocks
    private BookingController bookingController;
    private static ObjectMapper objectMapper;
    private MockMvc mockMvc;


    private static LocalDateTime testLocalDateTime;

    private static BookingRequestDto testBookingRequestDto;
    private static BookingResponseDto[] testBookingResponseDtos;


    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testLocalDateTime = LocalDateTime.now().plusYears(1);

        testBookingRequestDto = new BookingRequestDto(1L, testLocalDateTime, testLocalDateTime.plusYears(1));

        testBookingResponseDtos = new BookingResponseDto[2];
        testBookingResponseDtos[0] = new BookingResponseDto(
                1L,
                testLocalDateTime,
                testLocalDateTime,
                BookingStatus.WAITING,
                new UserShortResponseDto(1L),
                new ItemShortResponseDto(1L, "ItemName1")
        );
        testBookingResponseDtos[1] = new BookingResponseDto(
                2L,
                testLocalDateTime,
                testLocalDateTime,
                BookingStatus.WAITING,
                new UserShortResponseDto(2L),
                new ItemShortResponseDto(2L, "ItemName2")
        );
    }

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }


    @Test
    void testAddBooking() throws Exception {
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(
                                new BookingRequestDto(1L, null, testLocalDateTime)
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(
                                new BookingRequestDto(1L, testLocalDateTime, null)
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(
                                new BookingRequestDto(1L, testLocalDateTime, testLocalDateTime)
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(
                                new BookingRequestDto(1L, testLocalDateTime.minusYears(2), testLocalDateTime)
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        when(mockBookingClient.addBooking(any(), anyLong()))
                .thenReturn(ResponseEntity.ok(testBookingResponseDtos[0]));
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(testBookingRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBookingResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$.start[0]", is(testBookingResponseDtos[0].getStart().getYear())))
                .andExpect(jsonPath("$.end[0]", is(testBookingResponseDtos[0].getEnd().getYear())))
                .andExpect(jsonPath("$.status", is(testBookingResponseDtos[0].getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(testBookingResponseDtos[0].getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(testBookingResponseDtos[0].getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(testBookingResponseDtos[0].getItem().getName())));
    }

    @Test
    void testGetBooking() throws Exception {
        when(mockBookingClient.getBooking(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok(testBookingResponseDtos[0]));
        mockMvc.perform(get("/bookings/1").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBookingResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$.start[0]", is(testBookingResponseDtos[0].getStart().getYear())))
                .andExpect(jsonPath("$.end[0]", is(testBookingResponseDtos[0].getEnd().getYear())))
                .andExpect(jsonPath("$.status", is(testBookingResponseDtos[0].getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(testBookingResponseDtos[0].getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(testBookingResponseDtos[0].getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(testBookingResponseDtos[0].getItem().getName())));
    }

    @Test
    void testGetAllBookings() throws Exception {
        when(mockBookingClient.getAllBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(List.of(testBookingResponseDtos[0], testBookingResponseDtos[1])));
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        mockMvc.perform(get("/bookings?state=ALL&from=1&size=2")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testBookingResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$[0].start[0]", is(testBookingResponseDtos[0].getStart().getYear())))
                .andExpect(jsonPath("$[0].end[0]", is(testBookingResponseDtos[0].getEnd().getYear())))
                .andExpect(jsonPath("$[0].status", is(testBookingResponseDtos[0].getStatus().toString())))
                .andExpect(jsonPath("$[0].booker.id", is(testBookingResponseDtos[0].getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(testBookingResponseDtos[0].getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(testBookingResponseDtos[0].getItem().getName())))
                .andExpect(jsonPath("$[1].id", is(testBookingResponseDtos[1].getId()), Long.class))
                .andExpect(jsonPath("$[1].start[0]", is(testBookingResponseDtos[1].getStart().getYear())))
                .andExpect(jsonPath("$[1].end[0]", is(testBookingResponseDtos[1].getEnd().getYear())))
                .andExpect(jsonPath("$[1].status", is(testBookingResponseDtos[1].getStatus().toString())))
                .andExpect(jsonPath("$[1].booker.id", is(testBookingResponseDtos[1].getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[1].item.id", is(testBookingResponseDtos[1].getItem().getId()), Long.class))
                .andExpect(jsonPath("$[1].item.name", is(testBookingResponseDtos[1].getItem().getName())));
    }

    @Test
    void testGetAllBookingsFromOwner() throws Exception {
        when(mockBookingClient.getAllBookingsFromOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok(List.of(testBookingResponseDtos[0], testBookingResponseDtos[1])));
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        mockMvc.perform(get("/bookings/owner?state=ALL&from=1&size=2")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testBookingResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$[0].start[0]", is(testBookingResponseDtos[0].getStart().getYear())))
                .andExpect(jsonPath("$[0].end[0]", is(testBookingResponseDtos[0].getEnd().getYear())))
                .andExpect(jsonPath("$[0].status", is(testBookingResponseDtos[0].getStatus().toString())))
                .andExpect(jsonPath("$[0].booker.id", is(testBookingResponseDtos[0].getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(testBookingResponseDtos[0].getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(testBookingResponseDtos[0].getItem().getName())))
                .andExpect(jsonPath("$[1].id", is(testBookingResponseDtos[1].getId()), Long.class))
                .andExpect(jsonPath("$[1].start[0]", is(testBookingResponseDtos[1].getStart().getYear())))
                .andExpect(jsonPath("$[1].end[0]", is(testBookingResponseDtos[1].getEnd().getYear())))
                .andExpect(jsonPath("$[1].status", is(testBookingResponseDtos[1].getStatus().toString())))
                .andExpect(jsonPath("$[1].booker.id", is(testBookingResponseDtos[1].getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[1].item.id", is(testBookingResponseDtos[1].getItem().getId()), Long.class))
                .andExpect(jsonPath("$[1].item.name", is(testBookingResponseDtos[1].getItem().getName())));
    }

    @Test
    void testUpdateBooking() throws Exception {
        when(mockBookingClient.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok(testBookingResponseDtos[0]));
        mockMvc.perform(patch("/bookings/1?approved=false").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testBookingResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$.start[0]", is(testBookingResponseDtos[0].getStart().getYear())))
                .andExpect(jsonPath("$.end[0]", is(testBookingResponseDtos[0].getEnd().getYear())))
                .andExpect(jsonPath("$.status", is(testBookingResponseDtos[0].getStatus().toString())))
                .andExpect(jsonPath("$.booker.id", is(testBookingResponseDtos[0].getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(testBookingResponseDtos[0].getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(testBookingResponseDtos[0].getItem().getName())));
    }
}
