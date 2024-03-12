package com.example;

import com.example.service.MessageService;

public class Main {
    public static void main(String[] args) {
        MessageService service = new MessageService();
        System.out.println(service.fetchMessage());

    }
}
