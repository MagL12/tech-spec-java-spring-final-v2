package com.matlakhov.techspecjavaspringfinalv2.controller;

import com.matlakhov.techspecjavaspringfinalv2.dto.SubscriptionDto;
import com.matlakhov.techspecjavaspringfinalv2.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
/**
 * Контроллер для управления подписками пользователей.
 * Обрабатывает запросы, связанные с добавлением, получением и удалением подписок для конкретного пользователя.
 */
@Slf4j
@RestController
@RequestMapping("/users/{id}/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    /**
     * Добавляет новую подписку для пользователя.
     *
     * @param userId   идентификатор пользователя
     * @param dto DTO с данными для создания подписки
     * @return ResponseEntity с созданным DTO подписки и статусом 201 (Created), включая заголовок Location
     * @throws org.springframework.web.bind.MethodArgumentNotValidException если данные в createDto не прошли валидацию
     * @throws com.example.exception.DuplicateResourceException если подписка уже существует
     * @throws com.example.exception.ResourceNotFoundException если пользователь не найден
     */
    @PostMapping
    public ResponseEntity<SubscriptionDto> addSubscription(
            @PathVariable("id") Long userId,
            @Valid @RequestBody SubscriptionDto dto) {
        log.info("Добавление подписки для пользователя с ID: {} и сервисом: {}", userId, dto.getServiceName());
        SubscriptionDto created = subscriptionService.addSubscription(userId, dto);
        log.info("Подписка добавлена с ID: {}", created.getId());
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
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions(@PathVariable("id") Long userId) {
        log.info("Получение подписок для пользователя с ID: {}", userId);
        List<SubscriptionDto> list = subscriptionService.getUserSubscriptions(userId);
        log.info("Найдено {} подписок для пользователя с ID: {}", list.size(), userId);
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
        log.info("Удаление подписки с ID: {} для пользователя с ID: {}", subId, userId);
        subscriptionService.deleteSubscription(userId, subId);
        log.info("Подписка удалена с ID: {}", subId);
        return ResponseEntity.noContent().build();
    }
}