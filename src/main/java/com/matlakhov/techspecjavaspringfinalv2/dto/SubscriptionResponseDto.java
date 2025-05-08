package com.matlakhov.techspecjavaspringfinalv2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
/**
 * DTO (Data Transfer Object) для ответа с данными о подписке.
 * Используется для передачи информации о подписке через API.
 */
@Getter
@Setter
public class SubscriptionResponseDto {
    /**
     * Уникальный идентификатор подписки.
     */
    @Schema(
            description = "Уникальный идентификатор подписки",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY // Только для чтения в Swagger
    )
    private Long id;
    /**
     * Название сервиса, на который оформлена подписка.
     *
     * @example "YouTube Premium,"
     */
    @NotNull
    @Schema(description = "Название сервиса", example = "YouTube Premium")
    private String serviceName;
}
