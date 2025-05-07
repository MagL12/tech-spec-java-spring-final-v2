package com.example.techspecjavaspringfinalv2.controller;

import com.example.techspecjavaspringfinalv2.dto.UserCreateDto;
import com.example.techspecjavaspringfinalv2.dto.UserResponseDto;
import com.example.techspecjavaspringfinalv2.dto.UserUpdateDto;
import com.example.techspecjavaspringfinalv2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
/**
 * Контроллер для управления пользователями.
 * Предоставляет REST API для создания, получения, обновления и удаления пользователей.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    /**
     * Создает нового пользователя.
     *
     * @param createDto DTO с данными для создания пользователя
     * @return ResponseEntity с созданным DTO пользователя и статусом 201 (Created), включая заголовок Location
     * @throws org.springframework.web.bind.MethodArgumentNotValidException если данные в createDto не прошли валидацию
     * @throws com.example.exception.DuplicateResourceException если имя пользователя или email уже существуют
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto createDto) {
        logger.info("Создание пользователя с именем: {}", createDto.getUsername());
        UserResponseDto created = userService.createUser(createDto);
        logger.info("Пользователь создан с ID: {}", created.getId());
        return ResponseEntity.created(URI.create("/users/" + created.getId())).body(created);
    }

    /**
     * Получает информацию о пользователе по его ID.
     *
     * @param id идентификатор пользователя
     * @return ResponseEntity с DTO пользователя и статусом 200 (OK)
     * @throws com.example.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") Long id) {
        logger.info("Получение пользователя с ID: {}", id);
        UserResponseDto user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param id идентификатор пользователя
     * @param updateDto DTO с данными для обновления пользователя
     * @return ResponseEntity с обновленным DTO пользователя и статусом 200 (OK)
     * @throws org.springframework.web.bind.MethodArgumentNotValidException если данные в updateDto не прошли валидацию
     * @throws com.example.exception.DuplicateResourceException если новое имя пользователя или email уже существуют
     * @throws com.example.exception.ResourceNotFoundException если пользователь не найден
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserUpdateDto updateDto) {
        logger.info("Обновление пользователя с ID: {}", id);
        UserResponseDto updated = userService.updateUser(id, updateDto);
        logger.info("Пользователь обновлен с ID: {}", updated.getId());
        return ResponseEntity.ok(updated);
    }

    /**
     * Удаляет пользователя по его ID.
     *
     * @param id идентификатор пользователя
     * @return ResponseEntity со статусом 204 (No Content)
     * @throws com.example.exception.ResourceNotFoundException если пользователь не найден
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        logger.info("Удаление пользователя с ID: {}", id);
        userService.deleteUser(id);
        logger.info("Пользователь удален с ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
