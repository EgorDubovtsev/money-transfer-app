package org.bank.exception;

public class AccountNotExist extends Exception {
    public AccountNotExist(String message) {
        super(message);
    }

    public AccountNotExist() {
    }
}
