package com.example.techspecjavaspringfinalv2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
/**
 * Сущность, представляющая подписку на сервис.
 * Хранит информацию о подписке и связь с пользователем, который её оформил.
 */
@Entity
@Table(name = "subscriptions")
@Data
public class Subscription {
    /**
     * Уникальный идентификатор подписки.
     * Генерируется автоматически с использованием стратегии IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название сервиса, на который оформлена подписка.
     * Не может быть null, так как является обязательным полем.
     *
     * @example "Netflix"
     */
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    /**
     * Пользователь, которому принадлежит подписка.
     * Связь многие-к-одному с сущностью User, ленивая загрузка.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}