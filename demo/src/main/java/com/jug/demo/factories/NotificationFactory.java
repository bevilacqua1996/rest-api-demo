package com.jug.demo.factories;

public class NotificationFactory {

    public Notification getNotifier(String type) {
        return switch (type) {
            case "email" -> new EmailNotification();
            case "sms" -> new SmsNotification();
            default -> throw new IllegalArgumentException("Tipo inválido");
        };
    }

}
