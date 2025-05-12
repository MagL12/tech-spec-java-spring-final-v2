package com.matlakhov.techspecjavaspringfinalv2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для ответа с данными о подписке.
 * Используется для передачи информации о подписке через API.
 */
@Getter
@Setter
public class SubscriptionDto {
    @Schema(description = "Уникальный идентификатор подписки", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull
    @Schema(description = "Название сервиса", example = "YouTube Premium")
    private String serviceName;

    @Schema(description = "Дата начала подписки", example = "2025-05-12T10:00:00")
    private LocalDateTime startDate;

    @Schema(description = "Дата окончания подписки", example = "2025-06-12T10:00:00")
    private LocalDateTime endDate;
}