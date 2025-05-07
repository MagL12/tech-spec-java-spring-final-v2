package com.example.techspecjavaspringfinalv2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
/**
 * DTO (Data Transfer Object) для обновления данных пользователя.
 * Используется для передачи обновленных данных (имя или email) через API.
 */
@Getter
@Setter
@Schema(description = "DTO для обновления пользователя")
public class UserUpdateDto {
    /**
     * Имя пользователя.
     * Может быть null, если обновление не требуется.
     *
     * @example "newusername"
     */
    @Schema(description = "Имя пользователя", example = "newusername")
    private String username;

    /**
     * Электронная почта пользователя.
     * Может быть null, если обновление не требуется.
     *
     * @example "newemail@example.com"
     */
    @Schema(description = "Электронная почта", example = "newemail@example.com")
    private String email;
}
