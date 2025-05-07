package com.example.techspecjavaspringfinalv2;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;

import com.example.techspecjavaspringfinalv2.dto.DtoMapper;
import com.example.techspecjavaspringfinalv2.dto.SubscriptionCreateDto;
import com.example.techspecjavaspringfinalv2.dto.SubscriptionResponseDto;
import com.example.techspecjavaspringfinalv2.dto.UserResponseDto;
import com.example.techspecjavaspringfinalv2.model.Subscription;
import com.example.techspecjavaspringfinalv2.model.User;
import com.example.techspecjavaspringfinalv2.repository.SubscriptionRepository;
import com.example.techspecjavaspringfinalv2.service.SubscriptionService;
import com.example.techspecjavaspringfinalv2.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserService userService;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private SubscriptionService subscriptionService; // Mockito автоматически внедрит моки

    @Test
    void testAddSubscription_UserExists_SubscriptionNotDuplicate_ShouldReturnSubscriptionResponseDto() {
        // Arrange
        Long userId = 1L;
        SubscriptionCreateDto createDto = new SubscriptionCreateDto();
        createDto.setServiceName("Netflix");

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(userId);
        userDto.setUsername("Alice");
        userDto.setEmail("alice@example.com");

        Subscription subscriptionEntity = new Subscription();
        subscriptionEntity.setId(101L);
        subscriptionEntity.setServiceName("Netflix");
        subscriptionEntity.setUser(new User());
        subscriptionEntity.getUser().setId(userId);

        SubscriptionResponseDto responseDto = new SubscriptionResponseDto();
        responseDto.setId(101L);
        responseDto.setServiceName("Netflix");

        when(userService.getUser(userId)).thenReturn(userDto);
        when(subscriptionRepository.existsByUserIdAndServiceName(userId, "Netflix")).thenReturn(false);
        when(dtoMapper.toSubscriptionDto(any(Subscription.class))).thenReturn(responseDto);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscriptionEntity);

        // Act
        SubscriptionResponseDto result = subscriptionService.addSubscription(userId, createDto);

        // Assert
        assertNotNull(result);
        assertEquals("Netflix", result.getServiceName());
        verify(subscriptionRepository, times(1)).existsByUserIdAndServiceName(userId, "Netflix");
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }
}