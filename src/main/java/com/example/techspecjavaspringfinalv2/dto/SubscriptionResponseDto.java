package com.example.techspecjavaspringfinalv2.dto;

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
    private Long id;
    /**
     * Название сервиса, на который оформлена подписка.
     *
     * @example "Netflix"
     */
    private String serviceName;
}
