package org.bank;

import org.bank.entity.Account;
import org.bank.exception.AccountNotExist;
import org.bank.service.AccountService;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UserThread extends Thread {
    private final AccountService accountService;
    private int operationsInEachThread = 1;
    private final List<Account> accounts;

    public UserThread(String name, AccountService accountService, List<Account> accounts) {
        super(name);
        this.accountService = accountService;
        this.accounts = accounts;
    }

    public void setOperationsInEachThread(int operationsInEachThread) {
        this.operationsInEachThread = operationsInEachThread;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < operationsInEachThread; i++) {
                Account accountFrom = accountService.getAccountFromListById(accounts, ThreadLocalRandom.current().nextInt(accounts.size()));
                Account accountTo = accountService.getAccountFromListById(accounts, ThreadLocalRandom.current().nextInt(accounts.size()));
                accountService.transfer(accountFrom, accountTo, ThreadLocalRandom.current().nextInt(100));
            }

        } catch ( AccountNotExist e) {
            e.printStackTrace();
        }
    }
}
