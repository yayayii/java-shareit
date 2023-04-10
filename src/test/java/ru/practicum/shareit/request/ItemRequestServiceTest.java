package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestFullResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository mockItemRequestRepository;
    @Mock
    private UserRepository mockUserRepository;
    private ItemRequestService itemRequestService;


    private static LocalDateTime testLocalDateTime;
    private static User testUser;

    private static ItemRequestRequestDto testItemRequestRequestDto;
    private static ItemRequest[] testItemRequests;
    private static ItemRequestFullResponseDto[] testItemRequestFullResponseDtos;


    @BeforeAll
    static void beforeAll() {
        testLocalDateTime = LocalDateTime.of(2001, 1, 1, 1, 1);
        testUser = new User(1, "UserName1", "UserEmail1");
        initTestItemRequest();
    }

    @BeforeEach
    void beforeEach() {
        itemRequestService = new ItemRequestServiceImpl(mockItemRequestRepository, mockUserRepository);
    }


    @Test
    void testAddItemRequest() {
        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemRequestService.addItemRequest(testItemRequestRequestDto, 1)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.findById(anyInt()))
                .thenReturn(Optional.of(testUser));
        when(mockItemRequestRepository.save(any()))
                .thenReturn(testItemRequests[0]);
        assertDoesNotThrow(() -> itemRequestService.addItemRequest(testItemRequestRequestDto, 1));
    }

    @Test
    void testGetItemRequest() {
        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemRequestService.getItemRequest(1, 1)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(true);
        when(mockItemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.empty());
        exception = assertThrows(
                NoSuchElementException.class,
                () -> itemRequestService.getItemRequest(1, 1)
        );
        assertEquals(
                "Item Request id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockItemRequestRepository.findById(anyInt()))
                .thenReturn(Optional.of(testItemRequests[0]));
        assertDoesNotThrow(() -> itemRequestService.getItemRequest(1, 1));
    }

    @Test
    void testGetOtherItemRequests() {
        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemRequestService.getOtherItemRequests(1, 1, 2)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(true);
        when(mockItemRequestRepository.findAllByRequester_IdNot(anyInt(), any()))
                .thenReturn(List.of(
                        testItemRequests[0], testItemRequests[1], testItemRequests[2], testItemRequests[3]
                ));
        assertEquals(
                List.of(testItemRequestFullResponseDtos[1], testItemRequestFullResponseDtos[2]),
                itemRequestService.getOtherItemRequests(1, 1, 2)
        );
    }

    @Test
    void testGetOwnItemRequests() {
        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(false);
        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> itemRequestService.getOwnItemRequests(1)
        );
        assertEquals(
                "User id = 1 doesn't exist.",
                exception.getMessage()
        );

        when(mockUserRepository.existsById(anyInt()))
                .thenReturn(true);
        when(mockItemRequestRepository.findAllByRequester_Id(anyInt(), any()))
                .thenReturn(List.of(testItemRequests[0], testItemRequests[1]));
        assertEquals(
                List.of(testItemRequestFullResponseDtos[0], testItemRequestFullResponseDtos[1]),
                itemRequestService.getOwnItemRequests(1)
        );
    }


    private static void initTestItemRequest() {
        testItemRequestRequestDto = new ItemRequestRequestDto("ItemRequestDescription1");

        testItemRequests = new ItemRequest[4];
        testItemRequests[0] = new ItemRequest(
                1, "ItemRequestDescription1", testLocalDateTime, testUser, Collections.emptyList()
        );
        testItemRequests[1] = new ItemRequest(
                2, "ItemRequestDescription2", testLocalDateTime, testUser, Collections.emptyList()
        );
        testItemRequests[2] = new ItemRequest(
                3, "ItemRequestDescription3", testLocalDateTime, testUser, Collections.emptyList()
        );
        testItemRequests[3] = new ItemRequest(
                4, "ItemRequestDescription4", testLocalDateTime, testUser, Collections.emptyList()
        );

        testItemRequestFullResponseDtos = new ItemRequestFullResponseDto[3];
        testItemRequestFullResponseDtos[0] = new ItemRequestFullResponseDto(
                1, "ItemRequestDescription1", testLocalDateTime, Collections.emptyList()
        );
        testItemRequestFullResponseDtos[1] = new ItemRequestFullResponseDto(
                2, "ItemRequestDescription2", testLocalDateTime, Collections.emptyList()
        );
        testItemRequestFullResponseDtos[2] = new ItemRequestFullResponseDto(
                3, "ItemRequestDescription3", testLocalDateTime, Collections.emptyList()
        );
    }
}
