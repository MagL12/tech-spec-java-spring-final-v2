package com.example.techspecjavaspringfinalv2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
/**
 * DTO (Data Transfer Object) для создания новой подписки.
 * Используется для передачи данных о сервисе при добавлении подписки через API.
 */
@Getter
@Setter
@Schema(description = "DTO для создания новой подписки")
public class SubscriptionCreateDto {
    /**
     * Название сервиса, на который оформляется подписка.
     * Должно быть не null, так как является обязательным полем.
     *
     * @example "Netflix"
     */
    @NotNull
    @Schema(description = "Название сервиса", example = "Netflix")
    private String serviceName;
}
