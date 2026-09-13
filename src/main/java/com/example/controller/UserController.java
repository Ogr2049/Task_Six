package com.example.controller;

import com.example.assembler.UserModelAssembler;
import com.example.dto.UserRequestDto;
import com.example.dto.UserResponseDto;
import com.example.dto.UserResponseHateoasDto;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller", description = "API для управления пользователями")
public class UserController {
    private static final Logger myLoggerInstance = LoggerFactory.getLogger(UserController.class);
    
    private final UserService myUserService;
    private final UserModelAssembler myUserModelAssembler;
    
    @Autowired
    public UserController(UserService myUserService, UserModelAssembler myUserModelAssembler) {
        this.myUserService = myUserService;
        this.myUserModelAssembler = myUserModelAssembler;
    }
    
    @Operation(
        summary = "Создание нового пользователя",
        description = "Создает нового пользователя с указанными данными"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                    content = @Content(schema = @Schema(implementation = UserResponseHateoasDto.class))),
        @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
        @ApiResponse(responseCode = "409", description = "Пользователь с таким email уже существует")
    })
    @PostMapping
    public ResponseEntity<UserResponseHateoasDto> myCreateUser(
            @Valid @RequestBody UserRequestDto myUserRequestDto) {
        
        myLoggerInstance.info("REST request to create user: {}", myUserRequestDto.getUserEmail());
        UserResponseDto myCreatedUser = myUserService.myCreateUser(myUserRequestDto);
        UserResponseHateoasDto myHateoasResponse = myUserModelAssembler.toModel(myCreatedUser);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(myHateoasResponse);
    }
    
    @Operation(
        summary = "Получение пользователя по ID",
        description = "Возвращает пользователя по указанному идентификатору"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseHateoasDto> myGetUserById(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable Long id) {
        
        myLoggerInstance.info("REST request to get user by ID: {}", id);
        UserResponseDto myUser = myUserService.myGetUserById(id);
        UserResponseHateoasDto myHateoasResponse = myUserModelAssembler.toModel(myUser);
        
        return ResponseEntity.ok(myHateoasResponse);
    }
    
    @Operation(
        summary = "Получение всех пользователей",
        description = "Возвращает список всех пользователей с HATEOAS ссылками"
    )
    @ApiResponse(responseCode = "200", description = "Список пользователей получен")
    @GetMapping
    public ResponseEntity<CollectionModel<UserResponseHateoasDto>> myGetAllUsers() {
        myLoggerInstance.info("REST request to get all users");
        List<UserResponseDto> myUsers = myUserService.myGetAllUsers();
        CollectionModel<UserResponseHateoasDto> myHateoasResponse = 
            myUserModelAssembler.toCollectionModel(myUsers);
        
        return ResponseEntity.ok(myHateoasResponse);
    }
    
    @Operation(
        summary = "Обновление пользователя",
        description = "Обновляет данные пользователя по ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь обновлен"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseHateoasDto> myUpdateUser(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto myUserRequestDto) {
        
        myLoggerInstance.info("REST request to update user with ID: {}", id);
        UserResponseDto myUpdatedUser = myUserService.myUpdateUser(id, myUserRequestDto);
        UserResponseHateoasDto myHateoasResponse = myUserModelAssembler.toModel(myUpdatedUser);
        
        return ResponseEntity.ok(myHateoasResponse);
    }
    
    @Operation(
        summary = "Удаление пользователя",
        description = "Удаляет пользователя по ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Пользователь удален"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> myDeleteUser(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable Long id) {
        
        myLoggerInstance.info("REST request to delete user with ID: {}", id);
        myUserService.myDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(
        summary = "Поиск пользователя по email",
        description = "Возвращает пользователя по email адресу"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseHateoasDto> myGetUserByEmail(
            @Parameter(description = "Email пользователя", required = true, example = "user@example.com")
            @PathVariable String email) {
        
        myLoggerInstance.info("REST request to get user by email: {}", email);
        UserResponseDto myUser = myUserService.myGetUserByEmail(email);
        UserResponseHateoasDto myHateoasResponse = myUserModelAssembler.toModel(myUser);
        
        return ResponseEntity.ok(myHateoasResponse);
    }
    
    @Operation(
        summary = "Проверка существования email",
        description = "Проверяет, зарегистрирован ли email в системе"
    )
    @ApiResponse(responseCode = "200", description = "Результат проверки")
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> myCheckEmailExists(
            @Parameter(description = "Email для проверки", required = true)
            @PathVariable String email) {
        
        myLoggerInstance.info("REST request to check if email exists: {}", email);
        boolean myExists = myUserService.myCheckEmailExists(email);
        return ResponseEntity.ok(myExists);
    }
}