package com.example.assembler;

import com.example.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserModelAssemblerTest {
    
    private UserModelAssembler myUserModelAssembler;
    
    @BeforeEach
    void mySetUp() {
        myUserModelAssembler = new UserModelAssembler();
    }
    
    @Test
    void myTestToModel() {
        UserResponseDto myUserResponseDto = new UserResponseDto(
            1L, 
            "Иван Иванов", 
            "ivan@example.com", 
            30, 
            LocalDateTime.now()
        );
        
        var myResult = myUserModelAssembler.toModel(myUserResponseDto);
        
        assertThat(myResult).isNotNull();
        assertThat(myResult.getUserId()).isEqualTo(1L);
        assertThat(myResult.getUserName()).isEqualTo("Иван Иванов");
        assertThat(myResult.getLinks()).isNotEmpty();
    }
    
    @Test
    void myTestToCollectionModel() {
        UserResponseDto myUser1 = new UserResponseDto(
            1L, "Иван Иванов", "ivan@example.com", 30, LocalDateTime.now()
        );
        
        UserResponseDto myUser2 = new UserResponseDto(
            2L, "Петр Петров", "petr@example.com", 25, LocalDateTime.now()
        );
        
        List<UserResponseDto> myUsers = Arrays.asList(myUser1, myUser2);
        
        CollectionModel<?> myResult = myUserModelAssembler.toCollectionModel(myUsers);
        
        assertThat(myResult).isNotNull();
        assertThat(myResult.getContent()).hasSize(2);
        assertThat(myResult.getLinks()).isNotEmpty();
    }
}