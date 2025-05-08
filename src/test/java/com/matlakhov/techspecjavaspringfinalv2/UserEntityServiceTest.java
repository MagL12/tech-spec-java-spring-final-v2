package com.matlakhov.techspecjavaspringfinalv2;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import com.matlakhov.techspecjavaspringfinalv2.dto.UserResponseDto;
import com.matlakhov.techspecjavaspringfinalv2.exception.DuplicateResourceException;
import com.matlakhov.techspecjavaspringfinalv2.exception.ResourceNotFoundException;
import com.matlakhov.techspecjavaspringfinalv2.mappers.UserMapper;
import com.matlakhov.techspecjavaspringfinalv2.model.UserEntity;
import com.matlakhov.techspecjavaspringfinalv2.repository.UserRepository;
import com.matlakhov.techspecjavaspringfinalv2.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser_ValidData_ShouldReturnUserResponseDto() {
        // Arrange
        UserResponseDto dto = new UserResponseDto();
        dto.setUsername("Alice");
        dto.setEmail("alice@example.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("Alice");
        userEntity.setEmail("alice@example.com");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setUsername("Alice");
        responseDto.setEmail("alice@example.com");

        when(userRepository.existsByUsername("Alice")).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenAnswer(inv -> {
            userEntity.setId(1L); // Эмулируем присвоение ID
            return userEntity;
        });
        when(userMapper.toDto(userEntity)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.createUser(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Alice", result.getUsername());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testCreateUser_UsernameAlreadyExists_ShouldThrowDuplicateResourceException() {
        // Arrange
        UserResponseDto dto = new UserResponseDto();
        dto.setUsername("Alice");
        dto.setEmail("alice@example.com");

        when(userRepository.existsByUsername("Alice")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(dto);
        });

        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void testCreateUser_EmailAlreadyExists_ShouldThrowDuplicateResourceException() {
        // Arrange
        UserResponseDto dto = new UserResponseDto();
        dto.setUsername("Alice");
        dto.setEmail("alice@example.com");

        when(userRepository.existsByUsername("Alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            userService.createUser(dto);
        });

        verify(userRepository, times(1)).existsByUsername("Alice");
        verify(userRepository, times(1)).existsByEmail("alice@example.com");
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void testGetUser_ExistingId_ShouldReturnUserResponseDto() {
        // Arrange
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUsername("Alice");
        userEntity.setEmail("alice@example.com");

        UserResponseDto expectedDto = new UserResponseDto();
        expectedDto.setId(userId);
        expectedDto.setUsername("Alice");
        expectedDto.setEmail("alice@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(expectedDto);

        // Act
        UserResponseDto result = userService.getUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUser_NonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUser(userId);
        });
    }

    @Test
    void testUpdateUser_ValidUpdate_ShouldReturnUpdatedUser() {
        // Arrange
        Long userId = 1L;
        UserResponseDto dto = new UserResponseDto();
        dto.setUsername("NewAlice");
        dto.setEmail("new@example.com");

        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);
        existingUser.setUsername("OldAlice");
        existingUser.setEmail("old@example.com");

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(userId);
        updatedUser.setUsername("NewAlice");
        updatedUser.setEmail("new@example.com");

        UserResponseDto expectedDto = new UserResponseDto();
        expectedDto.setId(userId);
        expectedDto.setUsername("NewAlice");
        expectedDto.setEmail("new@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("NewAlice")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(expectedDto);

        // Act
        UserResponseDto result = userService.updateUser(userId, dto);

        // Assert
        assertEquals("NewAlice", result.getUsername());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testUpdateUser_DuplicateUsername_ShouldThrowException() {
        // Arrange
        Long userId = 1L;
        UserResponseDto dto = new UserResponseDto();
        dto.setUsername("ExistingUser");

        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);
        existingUser.setUsername("OldUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("ExistingUser")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> {
            userService.updateUser(userId, dto);
        });
    }

    @Test
    void testDeleteUser_ExistingId_ShouldDeleteSuccessfully() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_NonExistingId_ShouldThrowException() {
        // Arrange
        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });
    }
}