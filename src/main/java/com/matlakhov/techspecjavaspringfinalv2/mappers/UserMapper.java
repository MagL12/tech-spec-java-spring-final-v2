package com.matlakhov.techspecjavaspringfinalv2.mappers;

import com.matlakhov.techspecjavaspringfinalv2.dto.UserResponseDto;
import com.matlakhov.techspecjavaspringfinalv2.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Преобразует сущность User в DTO для ответа клиенту.
     *
     * @param userEntity сущность пользователя
     * @return UserResponseDto объект DTO с данными пользователя
     */
    UserResponseDto toDto(UserEntity userEntity);

    UserEntity toEntity(UserResponseDto userDto);

}
