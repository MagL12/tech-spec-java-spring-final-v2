package com.matlakhov.techspecjavaspringfinalv2.repository;

import com.matlakhov.techspecjavaspringfinalv2.model.SubscriptionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
/**
 * Репозиторий для работы с сущностью Subscription.
 * Предоставляет методы для доступа и управления данными о подписках в базе данных.
 */
/**
 * Репозиторий для работы с сущностью SubscriptionEntity.
 * Предоставляет методы для выполнения операций CRUD и дополнительных запросов к базе данных.
 */
/**
 * Репозиторий для работы с сущностью SubscriptionEntity.
 */
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    @Query("SELECT s FROM SubscriptionEntity s WHERE s.userEntity.id = :userId AND (s.endDate IS NULL OR s.endDate > :now) AND s.isDeleted = false")
    List<SubscriptionEntity> findByUserEntityId(Long userId, @Param("now") LocalDateTime now);

    @Query("""
            SELECT s.serviceName, COUNT(s) 
              FROM SubscriptionEntity s 
             WHERE s.serviceName IS NOT NULL AND s.serviceName <> '' 
               AND (s.endDate IS NULL OR s.endDate > :now) 
               AND s.isDeleted = false 
          GROUP BY s.serviceName 
          ORDER BY COUNT(s) DESC
        """)
    List<Object[]> findTopSubscriptions(@Param("now") LocalDateTime now, Pageable pageable);

    boolean existsByUserEntityIdAndServiceName(Long userId, String serviceName);
}
