package com.example.techspecjavaspringfinalv2.controller;

import com.example.techspecjavaspringfinalv2.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для получения статистических данных о подписках.
 * Предоставляет эндпоинт для получения топ-3 популярных подписок.
 */
@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionStatsController {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionStatsController.class);
    private final SubscriptionService subscriptionService;

    /**
     * Получает список топ-3 самых популярных подписок.
     *
     * @return ResponseEntity со списком названий топ-3 популярных подписок и статусом 200 (OK)
     * @throws RuntimeException если возникнут ошибки при получении данных из сервиса
     */
    @GetMapping("/top")
    public ResponseEntity<List<String>> getTopSubscriptions() {
        logger.info("Запрос на получение топ-3 популярных подписок");

        List<String> topSubscriptions = subscriptionService.getTopSubscriptions();

        logger.info("Получен список топ-3 подписок: {}", topSubscriptions);

        return ResponseEntity.ok(topSubscriptions);
    }
}