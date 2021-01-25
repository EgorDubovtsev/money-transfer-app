package org.bank.service;

import org.bank.entity.Account;
import org.bank.exception.AccountNotExist;

import java.util.List;

public interface AccountService {
    void transfer(Account accountFrom, Account accountTo, long amount);

    Account getAccountFromListById(List<Account> accounts, int id) throws AccountNotExist;

    void saveAccount(Account account);

    void safeLockAccounts(Account firstAccount, Account secondAccount);

    void safeUnlockAccounts(Account firstAccount, Account secondAccount);
}