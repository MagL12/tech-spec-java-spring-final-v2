package com.matlakhov.techspecjavaspringfinalv2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * DTO (Data Transfer Object) для ответа с данными о пользователе.
 * Используется для передачи информации о пользователе и его подписках через API.
 */
@Getter
@Setter
public class UserDto {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Schema(
            description = "Уникальный идентификатор пользователя",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY // Только для чтения в Swagger
    )
    private Long id;

    /**
     * Имя пользователя.
     *
     * @example "testuser"
     */
    @Schema(
            description = "Имя пользователя",
            example = "testUserName"
    )
    private String username;

    /**
     * Электронная почта пользователя.
     *
     * @example "test@example.com"
     */
    @Schema(
            description = "Электронная почта",
            example = "test@example.com"
    )
    private String email;

    /**
     * Список подписок пользователя.
     * Содержит данные о всех подписках, связанных с пользователем.
     */
    @Schema(
            description = "Список подписок",
            accessMode = Schema.AccessMode.READ_ONLY, // Скрыть в запросах
            hidden = true // Полностью скрыть в Swagger UI
    )
    private List<SubscriptionDto> subscriptions;
}
