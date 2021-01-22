package org.bank.service;

import org.bank.entity.Account;
import org.bank.exception.InsufficientFundsException;

public interface AccountService {
    void transfer(Account accountFrom, Account accountTo, long amount) throws InsufficientFundsException, InterruptedException;
}