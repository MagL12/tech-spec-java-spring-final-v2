package com.example.techspecjavaspringfinalv2.dto;

import com.example.techspecjavaspringfinalv2.model.Subscription;
import com.example.techspecjavaspringfinalv2.model.User;
import org.mapstruct.Mapper;

/**
 * Интерфейс для маппинга между сущностями и DTO.
 * Используется для преобразования объектов модели (User, Subscription) в DTO
 * для передачи данных через API.
 */
@Mapper(componentModel = "spring")
public interface DtoMapper {
    /**
     * Преобразует сущность User в DTO для ответа клиенту.
     *
     * @param user сущность пользователя
     * @return UserResponseDto объект DTO с данными пользователя
     */
    UserResponseDto toUserDto(User user);

    /**
     * Преобразует сущность Subscription в DTO для ответа клиенту.
     *
     * @param subscription сущность подписки
     * @return SubscriptionResponseDto объект DTO с данными подписки
     */
    SubscriptionResponseDto toSubscriptionDto(Subscription subscription);
}
