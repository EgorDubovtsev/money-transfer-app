package org.bank;

import org.bank.dao.AccountDao;
import org.bank.entity.Account;
import org.bank.exception.AccountDeserializeException;
import org.bank.exception.InsufficientFundsException;
import org.bank.service.AccountService;

import java.util.Random;

public class UserThread extends Thread {
    private AccountService accountService;
    private int totalCountOfAccounts;
    private Random random;
    private AccountDao accountDao;
    private int operationsInEachThread = 1;

    public UserThread(String name, AccountService accountService, int totalCountOfAccounts, Random random, AccountDao accountDao) {
        super(name);
        this.accountService = accountService;
        this.totalCountOfAccounts = totalCountOfAccounts;
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
    public void run() {
        try {
            for (int i=0; i < operationsInEachThread; i++) {
                Account accountFrom = accountDao.getAccountById(random.nextInt(totalCountOfAccounts));
                Account accountTo = accountDao.getAccountById(random.nextInt(totalCountOfAccounts));
                accountService.transfer(accountFrom, accountTo, random.nextInt(100));
            }

        } catch (AccountDeserializeException | InsufficientFundsException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
