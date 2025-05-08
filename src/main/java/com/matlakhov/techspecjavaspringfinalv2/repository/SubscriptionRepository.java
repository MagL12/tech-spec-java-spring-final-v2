package com.matlakhov.techspecjavaspringfinalv2.repository;

import com.matlakhov.techspecjavaspringfinalv2.model.SubscriptionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * Репозиторий для работы с сущностью Subscription.
 * Предоставляет методы для доступа и управления данными о подписках в базе данных.
 */
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    /**
     * Находит все подписки пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return список подписок, принадлежащих пользователю
     */
    List<SubscriptionEntity> findByUserEntityId(Long userId);

    /**
     * Находит топ подписок по популярности.
     * Возвращает названия сервисов и количество подписок, отсортированных по убыванию количества.
     *
     * @param pageable объект для пагинации и сортировки результатов
     * @return список массивов, где каждый массив содержит название сервиса и количество подписок
     */
    @Query("""
                SELECT s.serviceName, COUNT(s) 
                  FROM SubscriptionEntity s 
                 WHERE s.serviceName IS NOT NULL AND s.serviceName <> '' 
              GROUP BY s.serviceName 
              ORDER BY COUNT(s) DESC
            """)
    List<Object[]> findTopSubscriptions(Pageable pageable);

    /**
     * Проверяет, существует ли подписка для пользователя с указанным названием сервиса.
     *
     * @param userId      идентификатор пользователя
     * @param serviceName название сервиса
     * @return true, если подписка существует, иначе false
     */
    boolean existsByUserEntityIdAndServiceName(Long userId, String serviceName);
}
