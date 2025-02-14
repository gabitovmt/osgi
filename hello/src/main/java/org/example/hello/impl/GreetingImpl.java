package org.example.hello.impl;

import org.example.hello.Greeting;

public class GreetingImpl implements Greeting {
    private final String name;

    public GreetingImpl(String name) {
        this.name = name;
    }

    @SuppressWarnings("java:S106")  // Тестовый пример. Можно проигнорировать
    @Override
    public void sayHello() {
        System.out.println("Hello, " + name + "!");
    }
}
