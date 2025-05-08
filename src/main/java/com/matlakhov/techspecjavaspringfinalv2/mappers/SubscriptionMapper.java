package com.matlakhov.techspecjavaspringfinalv2.mappers;

import com.matlakhov.techspecjavaspringfinalv2.dto.SubscriptionResponseDto;
import com.matlakhov.techspecjavaspringfinalv2.model.SubscriptionEntity;
import org.mapstruct.Mapper;

/**
 * Интерфейс для маппинга между сущностями и DTO.
 * Используется для преобразования объектов модели (User, Subscription) в DTO
 * для передачи данных через API.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionMapper {


    /**
     * Преобразует сущность Subscription в DTO для ответа клиенту.
     *
     * @param subscriptionEntity сущность подписки
     * @return SubscriptionResponseDto объект DTO с данными подписки
     */
    SubscriptionResponseDto toDto(SubscriptionEntity subscriptionEntity);

    SubscriptionEntity toEntity(SubscriptionResponseDto subscriptionDto);

}
