package com.example.techspecjavaspringfinalv2.controller;

import com.example.techspecjavaspringfinalv2.dto.SubscriptionCreateDto;
import com.example.techspecjavaspringfinalv2.dto.SubscriptionResponseDto;
import com.example.techspecjavaspringfinalv2.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
/**
 * Контроллер для управления подписками пользователей.
 * Обрабатывает запросы, связанные с добавлением, получением и удалением подписок для конкретного пользователя.
 */
@RestController
@RequestMapping("/users/{id}/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);
    private final SubscriptionService subscriptionService;

    /**
     * Добавляет новую подписку для пользователя.
     *
     * @param userId   идентификатор пользователя
     * @param createDto DTO с данными для создания подписки
     * @return ResponseEntity с созданным DTO подписки и статусом 201 (Created), включая заголовок Location
     * @throws org.springframework.web.bind.MethodArgumentNotValidException если данные в createDto не прошли валидацию
     * @throws com.example.exception.DuplicateResourceException если подписка уже существует
     * @throws com.example.exception.ResourceNotFoundException если пользователь не найден
     */
    @PostMapping
    public ResponseEntity<SubscriptionResponseDto> addSubscription(
            @PathVariable("id") Long userId,
            @Valid @RequestBody SubscriptionCreateDto createDto) {
        logger.info("Добавление подписки для пользователя с ID: {} и сервисом: {}", userId, createDto.getServiceName());
        SubscriptionResponseDto created = subscriptionService.addSubscription(userId, createDto);
        logger.info("Подписка добавлена с ID: {}", created.getId());
        return ResponseEntity
                .created(URI.create("/users/" + userId + "/subscriptions/" + created.getId()))
                .body(created);
    }

    /**
     * Получает список всех подписок пользователя.
     *
     * @param userId идентификатор пользователя
     * @return ResponseEntity со списком DTO подписок и статусом 200 (OK)
     * @throws com.example.exception.ResourceNotFoundException если пользователь не найден
     */
    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDto>> getSubscriptions(@PathVariable("id") Long userId) {
        logger.info("Получение подписок для пользователя с ID: {}", userId);
        List<SubscriptionResponseDto> list = subscriptionService.getUserSubscriptions(userId);
        logger.info("Найдено {} подписок для пользователя с ID: {}", list.size(), userId);
        return ResponseEntity.ok(list);
    }

    /**
     * Удаляет подписку пользователя.
     *
     * @param userId идентификатор пользователя
     * @param subId  идентификатор подписки
     * @return ResponseEntity со статусом 204 (No Content)
     * @throws com.example.exception.ResourceNotFoundException если подписка или пользователь не найдены
     */
    @DeleteMapping("/{sub_id}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable("id") Long userId,
            @PathVariable("sub_id") Long subId) {
        logger.info("Удаление подписки с ID: {} для пользователя с ID: {}", subId, userId);
        subscriptionService.deleteSubscription(userId, subId);
        logger.info("Подписка удалена с ID: {}", subId);
        return ResponseEntity.noContent().build();
    }
}