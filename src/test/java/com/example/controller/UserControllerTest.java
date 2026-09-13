package com.example.controller;

import com.example.assembler.UserModelAssembler;
import com.example.dto.UserRequestDto;
import com.example.dto.UserResponseDto;
import com.example.dto.UserResponseHateoasDto;
import com.example.exception.NotFoundException;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc myMockMvc;
    
    @Autowired
    private ObjectMapper myObjectMapper;
    
    @MockBean
    private UserService myUserService;
    
    @MockBean
    private UserModelAssembler myUserModelAssembler;
    
    private UserRequestDto myValidUserRequest;
    private UserResponseDto myUserResponse;
    private UserResponseHateoasDto myUserHateoasResponse;
    
    @BeforeEach
    void mySetUp() {
        myValidUserRequest = new UserRequestDto();
        myValidUserRequest.setUserName("Иван Иванов");
        myValidUserRequest.setUserEmail("ivan@example.com");
        myValidUserRequest.setUserAge(30);
        
        myUserResponse = new UserResponseDto();
        myUserResponse.setUserId(1L);
        myUserResponse.setUserName("Иван Иванов");
        myUserResponse.setUserEmail("ivan@example.com");
        myUserResponse.setUserAge(30);
        myUserResponse.setUserCreatedAt(LocalDateTime.now());
        
        myUserHateoasResponse = new UserResponseHateoasDto();
        myUserHateoasResponse.setUserId(1L);
        myUserHateoasResponse.setUserName("Иван Иванов");
        myUserHateoasResponse.setUserEmail("ivan@example.com");
        myUserHateoasResponse.setUserAge(30);
        myUserHateoasResponse.setUserCreatedAt(LocalDateTime.now());
    }
    
    @Test
    void myTestCreateUserSuccess() throws Exception {
        when(myUserService.myCreateUser(any(UserRequestDto.class))).thenReturn(myUserResponse);
        when(myUserModelAssembler.toModel(myUserResponse)).thenReturn(myUserHateoasResponse);
        
        myMockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(myValidUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userName").value("Иван Иванов"));
        
        verify(myUserService, times(1)).myCreateUser(any(UserRequestDto.class));
    }
    
    @Test
    void myTestCreateUserValidationFailure() throws Exception {
        UserRequestDto myInvalidRequest = new UserRequestDto();
        myInvalidRequest.setUserName(""); // пустое имя
        myInvalidRequest.setUserEmail("invalid-email");
        myInvalidRequest.setUserAge(150); // слишком большой возраст
        
        myMockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(myInvalidRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void myTestGetUserByIdSuccess() throws Exception {
        when(myUserService.myGetUserById(1L)).thenReturn(myUserResponse);
        when(myUserModelAssembler.toModel(myUserResponse)).thenReturn(myUserHateoasResponse);
        
        myMockMvc.perform(get("/api/v1/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
        
        verify(myUserService, times(1)).myGetUserById(1L);
    }
    
    @Test
    void myTestGetUserByIdNotFound() throws Exception {
        when(myUserService.myGetUserById(999L))
            .thenThrow(new NotFoundException("Пользователь не найден с ID: 999"));
        
        myMockMvc.perform(get("/api/v1/users/{id}", 999))
                .andExpect(status().isNotFound());
        
        verify(myUserService, times(1)).myGetUserById(999L);
    }
    
    @Test
    void myTestGetAllUsersSuccess() throws Exception {
        UserResponseDto mySecondUserResponse = new UserResponseDto();
        mySecondUserResponse.setUserId(2L);
        mySecondUserResponse.setUserName("Петр Петров");
        mySecondUserResponse.setUserEmail("petr@example.com");
        mySecondUserResponse.setUserAge(25);
        
        List<UserResponseDto> myUsers = Arrays.asList(myUserResponse, mySecondUserResponse);
        when(myUserService.myGetAllUsers()).thenReturn(myUsers);
        
        UserResponseHateoasDto mySecondHateoasResponse = new UserResponseHateoasDto();
        mySecondHateoasResponse.setUserId(2L);
        
        when(myUserModelAssembler.toCollectionModel(myUsers))
            .thenReturn(CollectionModel.of(Arrays.asList(myUserHateoasResponse, mySecondHateoasResponse)));
        
        myMockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk());
        
        verify(myUserService, times(1)).myGetAllUsers();
    }
    
    @Test
    void myTestUpdateUserSuccess() throws Exception {
        UserResponseDto myUpdatedResponse = new UserResponseDto();
        myUpdatedResponse.setUserId(1L);
        myUpdatedResponse.setUserName("Иван Обновленный");
        myUpdatedResponse.setUserEmail("ivan.updated@example.com");
        myUpdatedResponse.setUserAge(31);
        
        UserResponseHateoasDto myUpdatedHateoasResponse = new UserResponseHateoasDto();
        myUpdatedHateoasResponse.setUserId(1L);
        myUpdatedHateoasResponse.setUserName("Иван Обновленный");
        
        when(myUserService.myUpdateUser(eq(1L), any(UserRequestDto.class))).thenReturn(myUpdatedResponse);
        when(myUserModelAssembler.toModel(myUpdatedResponse)).thenReturn(myUpdatedHateoasResponse);
        
        myMockMvc.perform(put("/api/v1/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(myValidUserRequest)))
                .andExpect(status().isOk());
        
        verify(myUserService, times(1)).myUpdateUser(eq(1L), any(UserRequestDto.class));
    }
    
    @Test
    void myTestDeleteUserSuccess() throws Exception {
        doNothing().when(myUserService).myDeleteUser(1L);
        
        myMockMvc.perform(delete("/api/v1/users/{id}", 1))
                .andExpect(status().isNoContent());
        
        verify(myUserService, times(1)).myDeleteUser(1L);
    }
    
    @Test
    void myTestGetUserByEmailSuccess() throws Exception {
        when(myUserService.myGetUserByEmail("ivan@example.com")).thenReturn(myUserResponse);
        when(myUserModelAssembler.toModel(myUserResponse)).thenReturn(myUserHateoasResponse);
        
        myMockMvc.perform(get("/api/v1/users/email/{email}", "ivan@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("ivan@example.com"));
        
        verify(myUserService, times(1)).myGetUserByEmail("ivan@example.com");
    }
}