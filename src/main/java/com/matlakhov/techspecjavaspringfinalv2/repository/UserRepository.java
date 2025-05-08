package com.matlakhov.techspecjavaspringfinalv2.repository;

import com.matlakhov.techspecjavaspringfinalv2.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Репозиторий для работы с сущностью User.
 * Предоставляет методы для проверки существования пользователей по имени и email.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Проверяет, существует ли пользователь с указанным именем.
     *
     * @param username имя пользователя для проверки
     * @return true, если пользователь с таким именем существует, иначе false
     */
    boolean existsByUsername(String username);

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email email пользователя для проверки
     * @return true, если пользователь с таким email существует, иначе false
     */
    boolean existsByEmail(String email);
}