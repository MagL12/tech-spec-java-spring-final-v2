package com.matlakhov.techspecjavaspringfinalv2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Сущность, представляющая пользователя.
 * Хранит информацию о пользователе и его подписках.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity {
    /**
     * Уникальный идентификатор пользователя.
     * Генерируется автоматически с использованием стратегии IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя пользователя.
     * Должно быть уникальным и не null, так как является обязательным полем.
     *
     * @example "testuser"
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Электронная почта пользователя.
     * Должна быть уникальной, валидным email-адресом и не null.
     *
     * @example "test@example.com"
     */
    @Column(unique = true, nullable = false)
    @Email
    private String email;

    /**
     * Список подписок пользователя.
     * Связь один-ко-многим с сущностью Subscription, с каскадным удалением.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriptionEntity> subscriptionEntities;
}
