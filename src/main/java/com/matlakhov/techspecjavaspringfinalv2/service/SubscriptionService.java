package com.matlakhov.techspecjavaspringfinalv2.service;

import com.matlakhov.techspecjavaspringfinalv2.dto.SubscriptionDto;
import com.matlakhov.techspecjavaspringfinalv2.mappers.SubscriptionMapper;
import com.matlakhov.techspecjavaspringfinalv2.exception.DuplicateResourceException;
import com.matlakhov.techspecjavaspringfinalv2.exception.ResourceNotFoundException;
import com.matlakhov.techspecjavaspringfinalv2.model.SubscriptionEntity;
import com.matlakhov.techspecjavaspringfinalv2.model.UserEntity;
import com.matlakhov.techspecjavaspringfinalv2.repository.SubscriptionRepository;
import com.matlakhov.techspecjavaspringfinalv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Сервис для управления подписками.
 * Предоставляет методы для создания, получения, удаления и анализа подписок.
 */
/**
 * Сервис для управления подписками.
 */
@RequiredArgsConstructor
@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;

    @Transactional
    public SubscriptionDto addSubscription(Long userId, SubscriptionDto dto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (subscriptionRepository.existsByUserEntityIdAndServiceName(userId, dto.getServiceName())) {
            throw new DuplicateResourceException("Subscription already exists");
        }

        SubscriptionEntity sub = subscriptionMapper.toEntity(dto);
        sub.setUserEntity(userEntity);
        sub.setIsDeleted(false);
        sub.setStartDate(LocalDateTime.now());
        sub.setEndDate(null);
        SubscriptionEntity savedSubscription = subscriptionRepository.save(sub);
        return subscriptionMapper.toDto(savedSubscription);
    }

    @Transactional(readOnly = true)
    public List<SubscriptionDto> getUserSubscriptions(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<SubscriptionEntity> list = subscriptionRepository.findByUserEntityId(userId, LocalDateTime.now());
        return list.stream()
                .map(subscriptionMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteSubscription(Long userId, Long subId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SubscriptionEntity sub = subscriptionRepository.findById(subId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        if (!sub.getUserEntity().getId().equals(userId)) {
            throw new ResourceNotFoundException("Subscription does not belong to user");
        }
        sub.setIsDeleted(true);
        sub.setEndDate(LocalDateTime.now());
        subscriptionRepository.save(sub);
    }

    @Transactional(readOnly = true)
    public List<String> getTopSubscriptions() {
        return subscriptionRepository
                .findTopSubscriptions(LocalDateTime.now(), PageRequest.of(0, 3))
                .stream()
                .map(row -> (String) row[0])
                .toList();
    }
}