package com.matlakhov.techspecjavaspringfinalv2.repository;

import com.matlakhov.techspecjavaspringfinalv2.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью UserEntity.
 * Предоставляет методы для выполнения операций CRUD и дополнительных запросов к базе данных.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Проверяет, существует ли пользователь с указанным именем пользователя.
     *
     * @param username имя пользователя для проверки
     * @return true, если пользователь существует, иначе false
     */
    boolean existsByUsername(String username);

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email email для проверки
     * @return true, если пользователь существует, иначе false
     */
    boolean existsByEmail(String email);

    /**
     * Находит пользователя по ID, подгружая только активные подписки.
     *
     * @param id идентификатор пользователя
     * @return Optional с найденным пользователем, включая только активные подписки
     */
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.subscriptionEntities s WHERE u.id = :id AND (s.id IS NULL OR s.isDeleted = false)")
    Optional<UserEntity> findByIdWithSubscriptions(@Param("id") Long id);
}