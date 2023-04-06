package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

@ExtendWith(MockitoExtension.class)
public class UserEndpointTest {
    @Mock
    private UserService mockUserService;
    @InjectMocks
    private UserController userController;
    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();
    private UserRequestDto userRequestDto = new UserRequestDto("user", "user@user.com");
    private UserResponseDto userResponseDto1 = new UserResponseDto(1, "user1", "user1@user.com");
    private UserResponseDto userResponseDto2 = new UserResponseDto(2, "user2", "user2@user.com");

    @BeforeEach
    void beforeEach() {
        mvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void testAddUser() throws Exception {
        mvc.perform(
                post("/users")
                .content(mapper.writeValueAsString(new UserRequestDto(null, "user@user.com"))).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest()
        );
        mvc.perform(
                post("/users")
                .content(mapper.writeValueAsString(new UserRequestDto("user", null))).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest()
        );
        mvc.perform(
                post("/users")
                .content(mapper.writeValueAsString(new UserRequestDto("user", "user"))).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest()
        );

        when(mockUserService.addUser(any()))
                .thenReturn(userResponseDto1);
        mvc.perform(
                post("/users")
                .content(mapper.writeValueAsString(userRequestDto)).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(userResponseDto1.getId())))
                    .andExpect(jsonPath("$.name", is(userResponseDto1.getName())))
                    .andExpect(jsonPath("$.email", is(userResponseDto1.getEmail()))
        );
    }

    @Test
    void testGetUser() throws Exception {
        when(mockUserService.getUser(anyInt()))
                .thenReturn(userResponseDto1);
        mvc.perform(
                get("/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(userResponseDto1.getId())))
                    .andExpect(jsonPath("$.name", is(userResponseDto1.getName())))
                    .andExpect(jsonPath("$.email", is(userResponseDto1.getEmail()))
        );
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(mockUserService.getAllUsers())
                .thenReturn(List.of(userResponseDto1, userResponseDto2));
        mvc.perform(
                get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(userResponseDto1.getId())))
                    .andExpect(jsonPath("$[0].name", is(userResponseDto1.getName())))
                    .andExpect(jsonPath("$[0].email", is(userResponseDto1.getEmail())))
                    .andExpect(jsonPath("$[1].id", is(userResponseDto2.getId())))
                    .andExpect(jsonPath("$[1].name", is(userResponseDto2.getName())))
                    .andExpect(jsonPath("$[1].email", is(userResponseDto2.getEmail()))
        );
    }

    @Test
    void testUpdateUser() throws Exception {
        mvc.perform(
                patch("/users/1")
                .content(mapper.writeValueAsString(new UserRequestDto("user", "user"))).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest()
        );

        when(mockUserService.updateUser(anyInt(), any()))
                .thenReturn(userResponseDto1);
        mvc.perform(
                patch("/users/1")
                .content(mapper.writeValueAsString(userRequestDto)).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(userResponseDto1.getId())))
                    .andExpect(jsonPath("$.name", is(userResponseDto1.getName())))
                    .andExpect(jsonPath("$.email", is(userResponseDto1.getEmail()))
        );
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(mockUserService).deleteUser(anyInt());
        mvc.perform(
                delete("/users/1"))
                    .andExpect(status().isOk()
        );
    }
}
