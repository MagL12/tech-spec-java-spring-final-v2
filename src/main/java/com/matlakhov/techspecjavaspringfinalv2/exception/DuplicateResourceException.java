package com.matlakhov.techspecjavaspringfinalv2.exception;
/**
 * Пользовательское исключение, которое возникает при попытке создать или обновить ресурс (например, пользователя или подписку),
 * если ресурс с указанными данными (имя пользователя, email или название сервиса) уже существует.
 */
public class DuplicateResourceException extends RuntimeException{
    /**
     * Конструктор исключения с указанием сообщения об ошибке.
     *
     * @param message сообщение, описывающее причину исключения (например, "Username already exists")
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
