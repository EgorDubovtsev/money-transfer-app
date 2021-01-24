package org.bank.service;

import org.bank.entity.Account;
import org.bank.exception.AccountNotExist;
import org.bank.exception.InsufficientFundsException;

import java.util.List;

public interface AccountService {
    void transfer(Account accountFrom, Account accountTo, long amount) throws InsufficientFundsException, InterruptedException;

    Account getAccountFromListById(List<Account> accounts, int id) throws AccountNotExist;

    void saveAccount(Account account);
}