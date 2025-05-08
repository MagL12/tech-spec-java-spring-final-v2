package com.matlakhov.techspecjavaspringfinalv2;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;

import com.matlakhov.techspecjavaspringfinalv2.exception.ResourceNotFoundException;
import com.matlakhov.techspecjavaspringfinalv2.mappers.SubscriptionMapper;
import com.matlakhov.techspecjavaspringfinalv2.dto.SubscriptionResponseDto;
import com.matlakhov.techspecjavaspringfinalv2.dto.UserResponseDto;
import com.matlakhov.techspecjavaspringfinalv2.model.SubscriptionEntity;
import com.matlakhov.techspecjavaspringfinalv2.model.UserEntity;
import com.matlakhov.techspecjavaspringfinalv2.repository.SubscriptionRepository;
import com.matlakhov.techspecjavaspringfinalv2.service.SubscriptionService;
import com.matlakhov.techspecjavaspringfinalv2.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class SubscriptionEntityServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserService userService;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private SubscriptionService subscriptionService; // Mockito автоматически внедрит моки

    @Test
    void testAddSubscription_UserExists_SubscriptionNotDuplicate_ShouldReturnSubscriptionResponseDto() {
        // Arrange
        Long userId = 1L;
        SubscriptionResponseDto dto = new SubscriptionResponseDto();
        dto.setServiceName("Netflix");

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(userId);
        userDto.setUsername("Alice");
        userDto.setEmail("alice@example.com");

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        subscriptionEntity.setId(101L);
        subscriptionEntity.setServiceName("Netflix");
        subscriptionEntity.setUserEntity(new UserEntity());
        subscriptionEntity.getUserEntity().setId(userId);

        SubscriptionResponseDto responseDto = new SubscriptionResponseDto();
        responseDto.setId(101L);
        responseDto.setServiceName("Netflix");

        when(userService.getUser(userId)).thenReturn(userDto);
        when(subscriptionRepository.existsByUserEntityIdAndServiceName(userId, "Netflix")).thenReturn(false);
        when(subscriptionMapper.toDto(subscriptionEntity)).thenReturn(responseDto);
        when(subscriptionMapper.toEntity(dto)).thenReturn(subscriptionEntity);
        when(subscriptionRepository.save(any(SubscriptionEntity.class))).thenReturn(subscriptionEntity);

        // Act
        SubscriptionResponseDto result = subscriptionService.addSubscription(userId, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Netflix", result.getServiceName());
        verify(subscriptionRepository, times(1)).existsByUserEntityIdAndServiceName(userId, "Netflix");
        verify(subscriptionRepository, times(1)).save(any(SubscriptionEntity.class));
    }

    @Test
    void testGetUserSubscriptions_UserHasSubscriptions_ShouldReturnSubscriptions() {
        // Arrange
        Long userId = 1L;
        SubscriptionEntity sub1 = new SubscriptionEntity();
        sub1.setId(101L);
        sub1.setServiceName("Netflix");
        SubscriptionEntity sub2 = new SubscriptionEntity();
        sub2.setId(102L);
        sub2.setServiceName("YouTube Premium");

        List<SubscriptionEntity> subscriptionEntities = List.of(sub1, sub2);
        when(subscriptionRepository.findByUserEntityId(userId)).thenReturn(subscriptionEntities);

        SubscriptionResponseDto dto1 = new SubscriptionResponseDto();
        dto1.setId(101L);
        dto1.setServiceName("Netflix");

        SubscriptionResponseDto dto2 = new SubscriptionResponseDto();
        dto2.setId(102L);
        dto2.setServiceName("YouTube Premium");

        when(subscriptionMapper.toDto(sub1)).thenReturn(dto1);
        when(subscriptionMapper.toDto(sub2)).thenReturn(dto2);

        // Act
        List<SubscriptionResponseDto> result = subscriptionService.getUserSubscriptions(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Netflix", result.get(0).getServiceName());
        assertEquals("YouTube Premium", result.get(1).getServiceName());
        verify(subscriptionRepository, times(1)).findByUserEntityId(userId);
    }

    @Test
    void testGetUserSubscriptions_UserHasNoSubscriptions_ShouldReturnEmptyList() {
        // Arrange
        Long userId = 1L;
        when(subscriptionRepository.findByUserEntityId(userId)).thenReturn(Collections.emptyList());

        // Act
        List<SubscriptionResponseDto> result = subscriptionService.getUserSubscriptions(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subscriptionRepository, times(1)).findByUserEntityId(userId);
    }

    // --- deleteSubscription ---

    @Test
    void testDeleteSubscription_ExistingSubscription_BelongsToUser_ShouldDeleteSuccessfully() {
        // Arrange
        Long userId = 1L;
        Long subId = 101L;

        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setId(subId);

        UserEntity user = new UserEntity();
        user.setId(userId);
        subscription.setUserEntity(user);

        when(subscriptionRepository.findById(subId)).thenReturn(Optional.of(subscription));

        // Act
        subscriptionService.deleteSubscription(userId, subId);

        // Assert
        verify(subscriptionRepository, times(1)).deleteById(subId);
    }

    @Test
    void testDeleteSubscription_SubscriptionDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long userId = 1L;
        Long subId = 101L;

        when(subscriptionRepository.findById(subId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            subscriptionService.deleteSubscription(userId, subId);
        });

        assertEquals("Subscription not found", exception.getMessage());
        verify(subscriptionRepository, times(1)).findById(subId);
        verify(subscriptionRepository, never()).deleteById(subId);
    }

    @Test
    void testDeleteSubscription_SubscriptionNotBelongToUser_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long userId = 1L;
        Long subId = 101L;

        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setId(subId);

        UserEntity anotherUser = new UserEntity();
        anotherUser.setId(2L); // Другой пользователь
        subscription.setUserEntity(anotherUser);

        when(subscriptionRepository.findById(subId)).thenReturn(Optional.of(subscription));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            subscriptionService.deleteSubscription(userId, subId);
        });

        assertEquals("Subscription does not belong to user", exception.getMessage());
        verify(subscriptionRepository, times(1)).findById(subId);
        verify(subscriptionRepository, never()).deleteById(subId);
    }

    // --- getTopSubscriptions ---

    @Test
    void testGetTopSubscriptions_ReturnsTop3() {
        // Arrange
        when(subscriptionRepository.findTopSubscriptions(any()))
                .thenReturn(List.of(
                        new Object[]{"Netflix", 45},
                        new Object[]{"YouTube Premium", 30},
                        new Object[]{"VK Музыка", 20}
                ));

        // Act
        List<String> result = subscriptionService.getTopSubscriptions();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Netflix", result.get(0));
        assertEquals("YouTube Premium", result.get(1));
        assertEquals("VK Музыка", result.get(2));
        verify(subscriptionRepository, times(1)).findTopSubscriptions(any());
    }

    @Test
    void testGetTopSubscriptions_NoSubscriptions_ShouldReturnEmptyList() {
        // Arrange
        when(subscriptionRepository.findTopSubscriptions(any())).thenReturn(Collections.emptyList());

        // Act
        List<String> result = subscriptionService.getTopSubscriptions();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subscriptionRepository, times(1)).findTopSubscriptions(any());
    }
}