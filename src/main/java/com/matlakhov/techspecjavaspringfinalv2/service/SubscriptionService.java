package com.matlakhov.techspecjavaspringfinalv2.service;

import com.matlakhov.techspecjavaspringfinalv2.mappers.SubscriptionMapper;
import com.matlakhov.techspecjavaspringfinalv2.dto.SubscriptionResponseDto;
import com.matlakhov.techspecjavaspringfinalv2.dto.UserResponseDto;
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
import java.util.stream.Collectors;

/**
 * Сервис для управления подписками.
 * Предоставляет методы для добавления, получения, удаления подписок и получения статистики популярных подписок.
 */
@RequiredArgsConstructor
@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final SubscriptionMapper mapper;
    private final UserRepository userRepository;
    /**
     * Добавляет новую подписку для пользователя.
     *
     * @param userId идентификатор пользователя
     * @param dto DTO с данными для создания подписки
     * @return SubscriptionResponseDto с данными созданной подписки
     * @throws DuplicateResourceException если подписка на указанный сервис уже существует для пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Transactional
    public SubscriptionResponseDto addSubscription(Long userId, SubscriptionResponseDto dto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (subscriptionRepository.existsByUserEntityIdAndServiceName(userId, dto.getServiceName())) {
            throw new DuplicateResourceException("Subscription already exists");
        }

        SubscriptionEntity sub = mapper.toEntity(dto);
        sub.setUserEntity(userEntity);
        SubscriptionEntity savedSubscription = subscriptionRepository.save(sub);
        return mapper.toDto(savedSubscription);
    }

    /**
     * Получает список подписок пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список SubscriptionResponseDto с данными о подписках
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Transactional(readOnly = true)
    public List<SubscriptionResponseDto> getUserSubscriptions(Long userId) {
        List<SubscriptionEntity> list = subscriptionRepository.findByUserEntityId(userId);
        return list.stream()
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Удаляет подписку пользователя.
     *
     * @param userId идентификатор пользователя
     * @param subId идентификатор подписки
     * @throws ResourceNotFoundException если подписка не найдена или не принадлежит указанному пользователю
     */
    @Transactional
    public void deleteSubscription(Long userId, Long subId) {
        SubscriptionEntity sub = subscriptionRepository.findById(subId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        if (!sub.getUserEntity().getId().equals(userId)) {
            throw new ResourceNotFoundException("Subscription does not belong to user");
        }
        subscriptionRepository.deleteById(subId);
    }

    /**
     * Получает топ-3 популярных подписок.
     *
     * @return список названий топ-3 сервисов по популярности
     */
    @Transactional(readOnly = true)
    public List<String> getTopSubscriptions() {
        return subscriptionRepository
                .findTopSubscriptions(PageRequest.of(0, 3))
                .stream()
                .map(row -> (String) row[0])
                .toList();
    }
}