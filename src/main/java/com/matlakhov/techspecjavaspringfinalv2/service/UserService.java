package com.matlakhov.techspecjavaspringfinalv2.service;

import com.matlakhov.techspecjavaspringfinalv2.dto.UserDto;
import com.matlakhov.techspecjavaspringfinalv2.mappers.SubscriptionMapper;
import com.matlakhov.techspecjavaspringfinalv2.exception.DuplicateResourceException;
import com.matlakhov.techspecjavaspringfinalv2.exception.ResourceNotFoundException;
import com.matlakhov.techspecjavaspringfinalv2.mappers.UserMapper;
import com.matlakhov.techspecjavaspringfinalv2.model.UserEntity;
import com.matlakhov.techspecjavaspringfinalv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления пользователями системы.
 * Предоставляет операции создания, получения, обновления и удаления пользователей,
 * а также проверку уникальности учётных данных.
 * <p>
 * Все операции выполняются в транзакционном контексте. Обрабатывает следующие исключения:
 * <ul>
 *   <li>{@link DuplicateResourceException} - при попытке создать дублирующиеся данные</li>
 *   <li>{@link ResourceNotFoundException} - при обращении к несуществующему ресурсу</li>
 * </ul>
 *
 * @see UserRepository
 * @see SubscriptionMapper
 */
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    /**
     * Создаёт нового пользователя в системе.
     * <p>
     * Пример использования:
     * <pre>{@code
     * UserCreateDto dto = new UserCreateDto("john_doe", "john@example.com");
     * UserResponseDto createdUser = userService.createUser(dto);
     * }</pre>
     *
     * @param dto DTO с данными для создания пользователя
     * @return DTO созданного пользователя
     * @throws DuplicateResourceException если имя пользователя или email уже существуют
     */
    @Transactional
    public UserDto createUser(UserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        UserEntity userEntity = userMapper.toEntity(dto);
        userEntity.setIsDeleted(false);
        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toDto(savedUser);
    }

    /**
     * Получает информацию о пользователе по идентификатору.
     *
     * @param id уникальный идентификатор пользователя
     * @return DTO пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {
        UserEntity userEntity = userRepository.findByIdWithSubscriptions(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (Boolean.TRUE.equals(userEntity.getIsDeleted())) {
            throw new ResourceNotFoundException("User not found");
        }
        return userMapper.toDto(userEntity);
    }

    /**
     * Обновляет данные существующего пользователя.
     * <p>
     * Обновляет только переданные в DTO поля. Проверяет уникальность новых значений.
     *
     * @param id  уникальный идентификатор пользователя
     * @param dto DTO с данными для обновления
     * @return обновлённое DTO пользователя
     * @throws ResourceNotFoundException  если пользователь не найден
     * @throws DuplicateResourceException если новые значения имени/email уже заняты
     */
    @Transactional
    public UserDto updateUser(Long id, UserDto dto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (Boolean.TRUE.equals(userEntity.getIsDeleted())) {
            throw new ResourceNotFoundException("User not found");
        }

        validateUserUpdate(userEntity, dto);

        if (dto.getUsername() != null) {
            userEntity.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null) {
            userEntity.setEmail(dto.getEmail());
        }

        UserEntity updatedUser = userRepository.save(userEntity);

        return userMapper.toDto(updatedUser);
    }

    /**
     * Удаляет пользователя из системы.
     *
     * @param id уникальный идентификатор пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Transactional
    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsDeleted(true);
        userRepository.save(user);
    }


    /**
     * Проверяет, не дублируются ли имя пользователя или email
     *
     * @param userEntity существующий пользователь из БД
     * @param dto        DTO с новыми данными
     * @throws DuplicateResourceException если имя или email уже заняты
     */
    private void validateUserUpdate(UserEntity userEntity, UserDto dto) {
        List<String> validationErrors = new ArrayList<>();

        if (dto.getUsername() != null && !dto.getUsername().equals(userEntity.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                validationErrors.add("Username already exists");
            }
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(userEntity.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                validationErrors.add("Email already exists");
            }
        }

        if (!validationErrors.isEmpty()) {
            throw new DuplicateResourceException(String.join("; ", validationErrors));
        }
    }
}