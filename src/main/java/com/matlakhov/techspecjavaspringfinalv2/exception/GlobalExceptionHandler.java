package com.matlakhov.techspecjavaspringfinalv2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для REST API.
 * Предоставляет централизованную обработку различных типов исключений,
 * возвращая соответствующие HTTP-статусы и сообщения об ошибках.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение ResourceNotFoundException, возникающее при отсутствии ресурса.
     *
     * @param ex исключение, содержащее сообщение об отсутствии ресурса
     * @return Map с ключом "error" и значением сообщения об ошибке, статус 404 (Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handleNotFound(ResourceNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    /**
     * Обрабатывает исключение DuplicateResourceException, возникающее при попытке создать дублирующийся ресурс.
     *
     * @param ex исключение, содержащее сообщение о конфликте (например, дубликат имени или email)
     * @return Map с ключом "error" и значением сообщения об ошибке, статус 409 (Conflict)
     */
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String,String> handleDuplicate(DuplicateResourceException ex) {
        return Map.of("error", ex.getMessage());
    }

    /**
     * Обрабатывает исключение MethodArgumentNotValidException, возникающее при некорректной валидации входных данных.
     *
     * @param ex исключение, содержащее информацию о полях с ошибками валидации
     * @return Map с ключами (имена полей) и значениями (сообщения об ошибках), статус 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleValidation(MethodArgumentNotValidException ex) {
        Map<String,String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return errors;
    }

    /**
     * Обрабатывает все необработанные исключения как запасной вариант.
     *
     * @param ex общее исключение, возникающее при внутренних ошибках
     * @return Map с ключом "error" и значением "Internal server error", статус 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,String> handleAll(Exception ex) {
        return Map.of("error", "Internal server error");
    }
}
