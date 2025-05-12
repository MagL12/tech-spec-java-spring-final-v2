package com.matlakhov.techspecjavaspringfinalv2.controller;

import com.matlakhov.techspecjavaspringfinalv2.dto.UserDto;
import com.matlakhov.techspecjavaspringfinalv2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
/**
 * Контроллер для управления пользователями.
 * Предоставляет REST API для создания, получения, обновления и удаления пользователей.
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
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
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        log.info("Создание пользователя с именем: {}", dto.getUsername());
        UserDto created = userService.createUser(dto);
        log.info("Пользователь создан с ID: {}", created.getId());
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
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        log.info("Получение пользователя с ID: {}", id);
        UserDto user = userService.getUser(id);
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
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDto updateDto) {
        log.info("Обновление пользователя с ID: {}", id);
        UserDto updated = userService.updateUser(id, updateDto);
        log.info("Пользователь обновлен с ID: {}", updated.getId());
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
        log.info("Удаление пользователя с ID: {}", id);
        userService.deleteUser(id);
        log.info("Пользователь помечен как удаленный с ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
