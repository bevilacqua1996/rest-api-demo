package com.jug.demo.factories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NotificationFactory Unit Tests")
class NotificationFactoryTest {

    private final NotificationFactory notificationFactory = new NotificationFactory();

    @Test
    @DisplayName("should return EmailNotification when type is 'email'")
    void shouldReturnEmailNotification() {
        Notification notification = notificationFactory.getNotifier("email");
        assertNotNull(notification);
        assertTrue(notification instanceof EmailNotification);

        // Testando o método send
        notification.send("Test email message");
    }

    @Test
    @DisplayName("should return SmsNotification when type is 'sms'")
    void shouldReturnSmsNotification() {
        Notification notification = notificationFactory.getNotifier("sms");
        assertNotNull(notification);
        assertTrue(notification instanceof SmsNotification);

        // Testando o método send
        notification.send("Test SMS message");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for invalid type")
    void shouldThrowExceptionForInvalidType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationFactory.getNotifier("invalid");
        });

        assertEquals("Tipo inválido", exception.getMessage());
    }
}