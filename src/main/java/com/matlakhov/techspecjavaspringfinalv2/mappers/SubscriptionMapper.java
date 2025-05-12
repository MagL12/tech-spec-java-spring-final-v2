package com.matlakhov.techspecjavaspringfinalv2.mappers;

import com.matlakhov.techspecjavaspringfinalv2.dto.SubscriptionDto;
import com.matlakhov.techspecjavaspringfinalv2.model.SubscriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Интерфейс для маппинга между сущностями и DTO.
 * Используется для преобразования объектов модели (User, Subscription) в DTO
 * для передачи данных через API.
 */
/**
 * Интерфейс для маппинга между сущностью Subscription и DTO.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    SubscriptionDto toDto(SubscriptionEntity subscriptionEntity);

    @Mapping(target = "userEntity", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    SubscriptionEntity toEntity(SubscriptionDto subscriptionDto);
}
