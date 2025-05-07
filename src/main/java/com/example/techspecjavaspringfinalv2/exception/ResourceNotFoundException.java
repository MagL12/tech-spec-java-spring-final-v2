package com.example.techspecjavaspringfinalv2.exception;
/**
 * Пользовательское исключение, которое возникает при попытке доступа к ресурсу (например, пользователю или подписке),
 * который не существует в базе данных.
 */
public class ResourceNotFoundException extends RuntimeException{
    /**
     * Конструктор исключения с указанием сообщения об ошибке.
     *
     * @param message сообщение, описывающее причину исключения (например, "User not found")
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
