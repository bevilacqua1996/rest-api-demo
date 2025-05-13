package com.jug.demo.singletons;

public class PrintSingleton {

    private static PrintSingleton instance;

    private PrintSingleton() {
        // Construtor privado para evitar instância externa
    }

    public static PrintSingleton getInstance() {
        if (instance == null) {
            instance = new PrintSingleton();
        }
        return instance;
    }

    public void print(String message) {
        System.out.println(message);
    }

}
