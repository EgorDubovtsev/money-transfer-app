package org.bank;

import org.bank.dao.AccountDao;
import org.bank.entity.Account;
import org.bank.exception.AccountDeserializeException;
import org.bank.exception.InsufficientFundsException;
import org.bank.service.AccountService;

import java.util.List;
import java.util.Random;

public class UserThread extends Thread {
    private AccountService accountService;
    private Random random;
    private AccountDao accountDao;
    private int operationsInEachThread = 1;
    private List<Account> accounts;

    public UserThread(String name, AccountService accountService, List<Account> accounts, Random random, AccountDao accountDao) {
        super(name);
        this.accountService = accountService;
        this.accounts = accounts;
        this.random = random;
        this.accountDao = accountDao;
    }

    public int getOperationsInEachThread() {
        return operationsInEachThread;
    }

    public void setOperationsInEachThread(int operationsInEachThread) {
        this.operationsInEachThread = operationsInEachThread;
    }

    @Override
    public void run() {//todo locks in each acc, find deadlock problem
        try {
            for (int i = 0; i < operationsInEachThread; i++) {
                Account accountFrom = accountService.getAccountFromListById(accounts, random.nextInt(accounts.size()));
                Account accountTo = accountService.getAccountFromListById(accounts, random.nextInt(accounts.size()));
                accountService.transfer(accountFrom, accountTo, random.nextInt(100));
            }

        } catch (InsufficientFundsException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
