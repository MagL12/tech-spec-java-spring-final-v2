package com.example.techspecjavaspringfinalv2;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import com.example.techspecjavaspringfinalv2.dto.DtoMapper;
import com.example.techspecjavaspringfinalv2.dto.UserCreateDto;
import com.example.techspecjavaspringfinalv2.dto.UserResponseDto;
import com.example.techspecjavaspringfinalv2.exception.DuplicateResourceException;
import com.example.techspecjavaspringfinalv2.model.User;
import com.example.techspecjavaspringfinalv2.repository.UserRepository;
import com.example.techspecjavaspringfinalv2.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser_ValidData_ShouldReturnUserResponseDto() {
        // Arrange
        UserCreateDto createDto = new UserCreateDto();
        createDto.setUsername("Alice");
        createDto.setEmail("alice@example.com");

        User userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("Alice");
        userEntity.setEmail("alice@example.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setUsername("Alice");
        responseDto.setEmail("alice@example.com");

        when(userRepository.existsByUsername("Alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(userEntity);
        when(dtoMapper.toUserDto(userEntity)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.createUser(createDto);

        // Assert
        assertNotNull(result);
        assertEquals("Alice", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_UsernameAlreadyExists_ShouldThrowDuplicateResourceException() {
        // Arrange
        UserCreateDto createDto = new UserCreateDto();
        createDto.setUsername("Alice");
        createDto.setEmail("alice@example.com");

        when(userRepository.existsByUsername("Alice")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(createDto);
        });

        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailAlreadyExists_ShouldThrowDuplicateResourceException() {
        // Arrange
        UserCreateDto createDto = new UserCreateDto();
        createDto.setUsername("Alice");
        createDto.setEmail("alice@example.com");

        when(userRepository.existsByUsername("Alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(createDto);
        });

        verify(userRepository, times(1)).existsByUsername("Alice");
        verify(userRepository, times(1)).existsByEmail("alice@example.com");
        verify(userRepository, never()).save(any(User.class));
    }
}