package com.example.techspecjavaspringfinalv2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
/**
 * DTO (Data Transfer Object) для создания нового пользователя.
 * Используется для передачи данных при регистрации пользователя через API.
 */
@Getter
@Setter
@Schema(description = "DTO для создания нового пользователя")
public class UserCreateDto {
    /**
     * Имя пользователя.
     * Должно быть не null, так как является обязательным полем.
     *
     * @example "testuser"
     */
    @NotNull
    @Schema(description = "Имя пользователя", example = "testuser")
    private String username;

    /**
     * Электронная почта пользователя.
     * Должна быть валидным email-адресом и не null, так как является обязательным полем.
     *
     * @example "test@example.com"
     */
    @NotNull
    @Email
    @Schema(description = "Электронная почта", example = "test@example.com")
    private String email;
}
