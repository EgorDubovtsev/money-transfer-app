package org.bank;

import org.bank.service.ExecutionService;

public class Main {
    public static void main(String[] args) {
        new ExecutionService().startThreads();
    }
}
