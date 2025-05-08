package com.matlakhov.techspecjavaspringfinalv2.service;

import com.matlakhov.techspecjavaspringfinalv2.mappers.SubscriptionMapper;
import com.matlakhov.techspecjavaspringfinalv2.dto.UserResponseDto;
import com.matlakhov.techspecjavaspringfinalv2.exception.DuplicateResourceException;
import com.matlakhov.techspecjavaspringfinalv2.exception.ResourceNotFoundException;
import com.matlakhov.techspecjavaspringfinalv2.mappers.UserMapper;
import com.matlakhov.techspecjavaspringfinalv2.model.UserEntity;
import com.matlakhov.techspecjavaspringfinalv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

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
    private final UserMapper mapper;


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
    public UserResponseDto createUser(UserResponseDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        UserEntity userEntity = mapper.toEntity(dto);
        UserEntity savedUser = userRepository.save(userEntity);
        return mapper.toDto(savedUser);
    }

    /**
     * Получает информацию о пользователе по идентификатору.
     *
     * @param id уникальный идентификатор пользователя
     * @return DTO пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapper.toDto(userEntity);
    }

    /**
     * Обновляет данные существующего пользователя.
     * <p>
     * Обновляет только переданные в DTO поля. Проверяет уникальность новых значений.
     *
     * @param id  уникальный идентификатор пользователя
     * @param dto DTO с данными для обновления
     * @return обновлённое DTO пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     * @throws DuplicateResourceException если новые значения имени/email уже заняты
     */
    @Transactional
    public UserResponseDto updateUser(Long id, UserResponseDto dto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (dto.getUsername() != null && !dto.getUsername().equals(userEntity.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new DuplicateResourceException("Username already exists");
            }
            userEntity.setUsername(dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(userEntity.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateResourceException("Email already exists");
            }
            userEntity.setEmail(dto.getEmail());
        }
        UserEntity updated = userRepository.save(userEntity);
        return mapper.toDto(updated);
    }

    /**
     * Удаляет пользователя из системы.
     *
     * @param id уникальный идентификатор пользователя
     * @throws ResourceNotFoundException если пользователь не найден
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}