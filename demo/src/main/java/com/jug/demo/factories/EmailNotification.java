package com.jug.demo.factories;

import com.jug.demo.singletons.PrintSingleton;

public class EmailNotification implements Notification {

    @Override
    public void send(String message) {
        // TODO: Implement email sending logic
        PrintSingleton.getInstance().print("Sending email to client: " + message);
    }
}
