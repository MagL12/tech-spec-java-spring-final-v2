package com.matlakhov.techspecjavaspringfinalv2.mappers;

import com.matlakhov.techspecjavaspringfinalv2.dto.UserDto;
import com.matlakhov.techspecjavaspringfinalv2.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Интерфейс для маппинга между сущностью UserEntity и DTO.
 * Используется для преобразования объектов модели User в DTO и обратно.
 */
@Mapper(componentModel = "spring", uses = SubscriptionMapper.class)
public interface UserMapper {

    /**
     * Преобразует сущность User в DTO для ответа клиенту.
     *
     * @param userEntity сущность пользователя
     * @return UserDto объект DTO с данными пользователя
     */
    @Mapping(source = "isDeleted", target = ".", ignore = true)
    @Mapping(source = "subscriptionEntities", target = "subscriptions")
    UserDto toDto(UserEntity userEntity);

    /**
     * Преобразует DTO в сущность User.
     *
     * @param userDto объект DTO с данными пользователя
     * @return UserEntity сущность пользователя
     */
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "subscriptionEntities", ignore = true)
    UserEntity toEntity(UserDto userDto);
}
