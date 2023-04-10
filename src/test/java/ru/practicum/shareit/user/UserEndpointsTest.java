package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserEndpointsTest {
    @Mock
    private UserService mockUserService;
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
        testUserResponseDtos[0] = new UserResponseDto(1, "UserName1", "User@Email1.com");
        testUserResponseDtos[1] = new UserResponseDto(2, "UserName2", "User@Email2.com");
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

        when(mockUserService.addUser(any()))
                .thenReturn(testUserResponseDtos[0]);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUserRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserResponseDtos[0].getId())))
                .andExpect(jsonPath("$.name", is(testUserResponseDtos[0].getName())))
                .andExpect(jsonPath("$.email", is(testUserResponseDtos[0].getEmail())));
    }

    @Test
    void testGetUser() throws Exception {
        when(mockUserService.getUser(anyInt()))
                .thenReturn(testUserResponseDtos[0]);
        mockMvc.perform(get("/users/1")
                        .content(objectMapper.writeValueAsString(testUserRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserResponseDtos[0].getId())))
                .andExpect(jsonPath("$.name", is(testUserResponseDtos[0].getName())))
                .andExpect(jsonPath("$.email", is(testUserResponseDtos[0].getEmail())));
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(mockUserService.getAllUsers())
                .thenReturn(List.of(testUserResponseDtos[0], testUserResponseDtos[1]));
        mockMvc.perform(get("/users")
                        .content(objectMapper.writeValueAsString(testUserRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testUserResponseDtos[0].getId())))
                .andExpect(jsonPath("$[0].name", is(testUserResponseDtos[0].getName())))
                .andExpect(jsonPath("$[0].email", is(testUserResponseDtos[0].getEmail())))
                .andExpect(jsonPath("$[1].id", is(testUserResponseDtos[1].getId())))
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

        when(mockUserService.updateUser(anyInt(), any()))
                .thenReturn(testUserResponseDtos[0]);
        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(testUserRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserResponseDtos[0].getId())))
                .andExpect(jsonPath("$.name", is(testUserResponseDtos[0].getName())))
                .andExpect(jsonPath("$.email", is(testUserResponseDtos[0].getEmail())));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(mockUserService).deleteUser(anyInt());
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}
