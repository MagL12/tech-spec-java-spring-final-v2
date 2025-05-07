package com.example.techspecjavaspringfinalv2.service;

import com.example.techspecjavaspringfinalv2.dto.DtoMapper;
import com.example.techspecjavaspringfinalv2.dto.SubscriptionCreateDto;
import com.example.techspecjavaspringfinalv2.dto.SubscriptionResponseDto;
import com.example.techspecjavaspringfinalv2.dto.UserResponseDto;
import com.example.techspecjavaspringfinalv2.exception.DuplicateResourceException;
import com.example.techspecjavaspringfinalv2.exception.ResourceNotFoundException;
import com.example.techspecjavaspringfinalv2.model.Subscription;
import com.example.techspecjavaspringfinalv2.model.User;
import com.example.techspecjavaspringfinalv2.repository.SubscriptionRepository;
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
    private final DtoMapper mapper;

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
    public SubscriptionResponseDto addSubscription(Long userId, SubscriptionCreateDto dto) {
        UserResponseDto userDto = userService.getUser(userId); // проверка существования
        User user = new User(); user.setId(userDto.getId()); // для existsBy
        if (subscriptionRepository.existsByUserIdAndServiceName(userId, dto.getServiceName())) {
            throw new DuplicateResourceException("Subscription already exists");
        }
        Subscription sub = new Subscription();
        sub.setServiceName(dto.getServiceName());
        sub.setUser(user);
        Subscription saved = subscriptionRepository.save(sub);
        return mapper.toSubscriptionDto(saved);
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
        List<Subscription> list = subscriptionRepository.findByUserId(userId);
        return list.stream()
                .map(mapper::toSubscriptionDto)
                .collect(Collectors.toList());
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
        Subscription sub = subscriptionRepository.findById(subId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        if (!sub.getUser().getId().equals(userId)) {
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
                .collect(Collectors.toList());
    }
}