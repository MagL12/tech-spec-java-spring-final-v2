package com.matlakhov.techspecjavaspringfinalv2;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;

import com.matlakhov.techspecjavaspringfinalv2.dto.SubscriptionDto;
import com.matlakhov.techspecjavaspringfinalv2.exception.ResourceNotFoundException;
import com.matlakhov.techspecjavaspringfinalv2.mappers.SubscriptionMapper;
import com.matlakhov.techspecjavaspringfinalv2.model.SubscriptionEntity;
import com.matlakhov.techspecjavaspringfinalv2.model.UserEntity;
import com.matlakhov.techspecjavaspringfinalv2.repository.SubscriptionRepository;
import com.matlakhov.techspecjavaspringfinalv2.repository.UserRepository;
import com.matlakhov.techspecjavaspringfinalv2.service.SubscriptionService;
import com.matlakhov.techspecjavaspringfinalv2.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class SubscriptionEntityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void testAddSubscription_UserExists_SubscriptionNotDuplicate_ShouldReturnDto() {
        Long userId = 1L;
        SubscriptionDto dto = new SubscriptionDto();
        dto.setServiceName("Netflix");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        when(subscriptionRepository.existsByUserEntityIdAndServiceName(userId, "Netflix"))
                .thenReturn(false);

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setId(101L);
        subscriptionEntity.setServiceName("Netflix");
        subscriptionEntity.setUserEntity(userEntity);
        subscriptionEntity.setStartDate(LocalDateTime.now().minusDays(10));
        subscriptionEntity.setEndDate(null);
        subscriptionEntity.setIsDeleted(false);

        when(subscriptionMapper.toEntity(dto)).thenReturn(subscriptionEntity);
        when(subscriptionRepository.save(subscriptionEntity)).thenReturn(subscriptionEntity);

        SubscriptionDto responseDto = new SubscriptionDto();
        responseDto.setId(101L);
        responseDto.setServiceName("Netflix");
        responseDto.setStartDate(LocalDateTime.now().minusDays(10));
        responseDto.setEndDate(null);
        when(subscriptionMapper.toDto(subscriptionEntity)).thenReturn(responseDto);

        SubscriptionDto result = subscriptionService.addSubscription(userId, dto);

        assertNotNull(result);
        assertEquals(101L, result.getId());
        assertEquals("Netflix", result.getServiceName());
        verify(subscriptionRepository, times(1)).save(subscriptionEntity);
    }

    @Test
    void testGetUserSubscriptions_UserHasSubscriptions_ShouldReturnSubscriptions() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);

        SubscriptionEntity sub1 = new SubscriptionEntity();
        sub1.setId(101L);
        sub1.setServiceName("Netflix");
        sub1.setIsDeleted(false);
        sub1.setStartDate(LocalDateTime.now().minusDays(10));
        sub1.setEndDate(null);

        SubscriptionEntity sub2 = new SubscriptionEntity();
        sub2.setId(102L);
        sub2.setServiceName("YouTube Premium");
        sub2.setIsDeleted(false);
        sub2.setStartDate(LocalDateTime.now().minusDays(5));
        sub2.setEndDate(null);

        List<SubscriptionEntity> subscriptionEntities = List.of(sub1, sub2);
        when(subscriptionRepository.findByUserEntityId(eq(userId), any(LocalDateTime.class)))
                .thenReturn(subscriptionEntities);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        SubscriptionDto dto1 = new SubscriptionDto();
        dto1.setId(101L);
        dto1.setServiceName("Netflix");
        dto1.setStartDate(LocalDateTime.now().minusDays(10));
        dto1.setEndDate(null);

        SubscriptionDto dto2 = new SubscriptionDto();
        dto2.setId(102L);
        dto2.setServiceName("YouTube Premium");
        dto2.setStartDate(LocalDateTime.now().minusDays(5));
        dto2.setEndDate(null);

        when(subscriptionMapper.toDto(sub1)).thenReturn(dto1);
        when(subscriptionMapper.toDto(sub2)).thenReturn(dto2);

        List<SubscriptionDto> result = subscriptionService.getUserSubscriptions(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Netflix", result.get(0).getServiceName());
        assertEquals("YouTube Premium", result.get(1).getServiceName());
        verify(subscriptionRepository, times(1))
                .findByUserEntityId(eq(userId), any(LocalDateTime.class));
        verify(subscriptionMapper, times(2)).toDto(any(SubscriptionEntity.class));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserSubscriptions_UserHasNoSubscriptions_ShouldReturnEmptyList() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);

        when(subscriptionRepository.findByUserEntityId(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<SubscriptionDto> result = subscriptionService.getUserSubscriptions(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(subscriptionRepository, times(1))
                .findByUserEntityId(eq(userId), any(LocalDateTime.class));
        verify(subscriptionMapper, never()).toDto(any(SubscriptionEntity.class));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testDeleteSubscription_ExistingSubscription_BelongsToUser_ShouldDeleteSuccessfully() {
        Long userId = 1L;
        Long subId = 101L;

        UserEntity user = new UserEntity();
        user.setId(userId);

        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setId(subId);
        subscription.setServiceName("Netflix");
        subscription.setIsDeleted(false);
        subscription.setStartDate(LocalDateTime.now().minusDays(10));
        subscription.setEndDate(null);
        subscription.setUserEntity(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findById(subId)).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        subscriptionService.deleteSubscription(userId, subId);

        assertTrue(subscription.getIsDeleted());
        assertNotNull(subscription.getEndDate()); // Проверяем, что endDate установлено
        verify(subscriptionRepository, times(1)).findById(subId);
        verify(subscriptionRepository, times(1)).save(subscription);
        verify(subscriptionRepository, never()).deleteById(anyLong());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testDeleteSubscription_SubscriptionDoesNotExist_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;
        Long subId = 101L;

        // <-- добавляем мок для пользователя
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(subscriptionRepository.findById(subId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            subscriptionService.deleteSubscription(userId, subId);
        });

        assertEquals("Subscription not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(subscriptionRepository, times(1)).findById(subId);
        verify(subscriptionRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteSubscription_SubscriptionNotBelongToUser_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;
        Long subId = 101L;

        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setId(subId);
        UserEntity anotherUser = new UserEntity();
        anotherUser.setId(2L);
        subscription.setUserEntity(anotherUser);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(new UserEntity() {{ setId(userId); }}));
        when(subscriptionRepository.findById(subId))
                .thenReturn(Optional.of(subscription));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            subscriptionService.deleteSubscription(userId, subId);
        });
        assertEquals("Subscription does not belong to user", ex.getMessage());

        verify(subscriptionRepository, times(1)).findById(subId);
        verify(subscriptionRepository, never()).deleteById(subId);
    }

    @Test
    void testGetTopSubscriptions_ReturnsTop3() {
        when(subscriptionRepository.findTopSubscriptions(any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(List.of(
                        new Object[]{"Netflix", 45},
                        new Object[]{"YouTube Premium", 30},
                        new Object[]{"VK Музыка", 20}
                ));

        List<String> result = subscriptionService.getTopSubscriptions();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Netflix", result.get(0));
        assertEquals("YouTube Premium", result.get(1));
        assertEquals("VK Музыка", result.get(2));
        verify(subscriptionRepository, times(1)).findTopSubscriptions(any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void testGetTopSubscriptions_NoSubscriptions_ShouldReturnEmptyList() {
        when(subscriptionRepository.findTopSubscriptions(any(LocalDateTime.class), any(PageRequest.class))).thenReturn(Collections.emptyList());

        List<String> result = subscriptionService.getTopSubscriptions();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subscriptionRepository, times(1)).findTopSubscriptions(any(LocalDateTime.class), any(PageRequest.class));
    }
}