package org.bank.service;

import org.bank.dao.AccountDao;
import org.bank.entity.Account;
import org.bank.exception.InsufficientFundsException;
import org.slf4j.Logger;

import java.util.List;

public class SimpleAccountService implements AccountService {
    private Logger logger;
    private AccountDao accountDao;

    public SimpleAccountService(Logger logger, AccountDao accountDao) {
        this.logger = logger;
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(Account accountFrom, Account accountTo, long amount) throws InsufficientFundsException {
        logger.info("TRANSACTION FROM {} TO {}, AMOUNT:{}", accountFrom.getId(), accountTo.getId(), amount);
        if (accountFrom.getBalance() < amount) {
            throw new InsufficientFundsException("Incorrect amount");
        }
        accountFrom.lock();
        accountTo.lock();
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);
        accountFrom.unlock();
        accountTo.unlock();

        logger.debug(" {} new balance {} ", accountFrom.getId(), accountFrom.getBalance());
        logger.debug(" {} new balance {} ", accountTo.getId(), accountTo.getBalance());

    }

    @Override
    public Account getAccountFromListById(List<Account> accounts, int id) {
        return accounts.stream().filter(account -> account.getId() == id).findFirst().orElse(null);//todo: create exept
    }

    @Override
    public void saveAccount(Account account) {
        accountDao.saveAccountData(account);
    }
}
