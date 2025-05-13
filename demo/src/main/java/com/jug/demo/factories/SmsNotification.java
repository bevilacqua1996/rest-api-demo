package com.jug.demo.factories;

import com.jug.demo.singletons.PrintSingleton;

public class SmsNotification implements Notification {

    @Override
    public void send(String message) {
        // TODO: Implement SMS sending logic
        PrintSingleton.getInstance().print("Sending SMS to client: " + message);
    }
}
