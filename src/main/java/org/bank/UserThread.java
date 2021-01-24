package org.bank;

import org.bank.dao.AccountDao;
import org.bank.entity.Account;
import org.bank.exception.AccountDeserializeException;
import org.bank.exception.AccountNotExist;
import org.bank.exception.InsufficientFundsException;
import org.bank.service.AccountService;

import java.util.List;
import java.util.Random;

public class UserThread extends Thread {
    private AccountService accountService;
    private Random random;
    private int operationsInEachThread = 1;
    private List<Account> accounts;

    public UserThread(String name, AccountService accountService, List<Account> accounts, Random random) {
        super(name);
        this.accountService = accountService;
        this.accounts = accounts;
        this.random = random;

    }

    public int getOperationsInEachThread() {
        return operationsInEachThread;
    }

    public void setOperationsInEachThread(int operationsInEachThread) {
        this.operationsInEachThread = operationsInEachThread;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < operationsInEachThread; i++) {
                Account accountFrom = accountService.getAccountFromListById(accounts, random.nextInt(accounts.size()));
                Account accountTo = accountService.getAccountFromListById(accounts, random.nextInt(accounts.size()));
                accountService.transfer(accountFrom, accountTo, random.nextInt(100));
            }

        } catch (InsufficientFundsException | AccountNotExist e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
