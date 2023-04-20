package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.client.UserClient;
import ru.practicum.shareit.dto.UserRequestDto;
import ru.practicum.shareit.dto.UserResponseDto;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserClient mockUserClient;
    @InjectMocks
    private UserController userController;
    private static ObjectMapper objectMapper;
    private MockMvc mockMvc;


    private static UserRequestDto testUserRequestDto;
    private static UserResponseDto[] testUserResponseDtos;


    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();

        testUserRequestDto = new UserRequestDto("UserName1", "User@Email1.com");

        testUserResponseDtos = new UserResponseDto[2];
        testUserResponseDtos[0] = new UserResponseDto(1L, "UserName1", "User@Email1.com");
        testUserResponseDtos[1] = new UserResponseDto(2L, "UserName2", "User@Email2.com");
    }

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    void testAddUser() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(
                                new UserRequestDto(null, "User@Email1.com")
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(
                                new UserRequestDto("UserName1", null)
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(
                                new UserRequestDto("UserName1", "UserEmail1.com")
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        when(mockUserClient.addUser(any()))
                .thenReturn(ResponseEntity.ok(testUserResponseDtos[0]));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUserRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testUserResponseDtos[0].getName())))
                .andExpect(jsonPath("$.email", is(testUserResponseDtos[0].getEmail())));
    }

    @Test
    void testGetUser() throws Exception {
        when(mockUserClient.getUser(anyLong()))
                .thenReturn(ResponseEntity.ok(testUserResponseDtos[0]));
        mockMvc.perform(get("/users/1")
                        .content(objectMapper.writeValueAsString(testUserRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testUserResponseDtos[0].getName())))
                .andExpect(jsonPath("$.email", is(testUserResponseDtos[0].getEmail())));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(mockUserClient.getAllUsers())
                .thenReturn(ResponseEntity.ok(List.of(testUserResponseDtos[0], testUserResponseDtos[1])));
        mockMvc.perform(get("/users")
                        .content(objectMapper.writeValueAsString(testUserRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testUserResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(testUserResponseDtos[0].getName())))
                .andExpect(jsonPath("$[0].email", is(testUserResponseDtos[0].getEmail())))
                .andExpect(jsonPath("$[1].id", is(testUserResponseDtos[1].getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(testUserResponseDtos[1].getName())))
                .andExpect(jsonPath("$[1].email", is(testUserResponseDtos[1].getEmail())));
    }

    @Test
    void testUpdateUser() throws Exception {
        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(
                                new UserRequestDto("UserName1", "UserEmail1.com")
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        when(mockUserClient.updateUser(anyLong(), any()))
                .thenReturn(ResponseEntity.ok(testUserResponseDtos[0]));
        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(testUserRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserResponseDtos[0].getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testUserResponseDtos[0].getName())))
                .andExpect(jsonPath("$.email", is(testUserResponseDtos[0].getEmail())));
    }

    @Test
    void testDeleteUser() throws Exception {
        when(mockUserClient.deleteUser(anyLong())).thenReturn(ResponseEntity.ok(any()));
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}
