package com.example.techspecjavaspringfinalv2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * DTO (Data Transfer Object) для ответа с данными о пользователе.
 * Используется для передачи информации о пользователе и его подписках через API.
 */
@Getter
@Setter
public class UserResponseDto {

    /**
     * Уникальный идентификатор пользователя.
     */
    private Long id;

    /**
     * Имя пользователя.
     *
     * @example "testuser"
     */
    private String username;

    /**
     * Электронная почта пользователя.
     *
     * @example "test@example.com"
     */
    private String email;

    /**
     * Список подписок пользователя.
     * Содержит данные о всех подписках, связанных с пользователем.
     */
    private List<SubscriptionResponseDto> subscriptions;
}
