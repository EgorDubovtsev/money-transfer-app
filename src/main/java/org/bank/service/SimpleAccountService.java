package org.bank.service;

import org.bank.dao.AccountDao;
import org.bank.entity.Account;
import org.bank.exception.AccountNotExist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SimpleAccountService implements AccountService {
    private final AccountDao accountDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAccountService.class);

    public SimpleAccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(Account accountFrom, Account accountTo, long amount) {
        LOGGER.info("TRANSACTION FROM {} TO {}, AMOUNT:{}", accountFrom.getId(), accountTo.getId(), amount);
        if (accountFrom.getBalance() < amount) {
            LOGGER.error("Incorrect amount");

            return;
        }
        safeLockAccounts(accountFrom, accountTo);

        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);

        safeUnlockAccounts(accountFrom, accountTo);

        LOGGER.debug(" {} new balance: {} ", accountFrom.getId(), accountFrom.getBalance());
        LOGGER.debug(" {} new balance: {} ", accountTo.getId(), accountTo.getBalance());

    }

    @Override
    public Account getAccountFromListById(List<Account> accounts, int id) throws AccountNotExist {
        return accounts.stream()
                .filter(account -> account.getId() == id)
                .findFirst()
                .orElseThrow(AccountNotExist::new);
    }

    @Override
    public void saveAccount(Account account) {
        accountDao.saveAccountData(account);
    }

    @Override
    public void safeLockAccounts(Account firstAccount, Account secondAccount) {
        if (firstAccount.getId() < secondAccount.getId()) {
            firstAccount.lock();
            secondAccount.lock();

        } else {
            secondAccount.lock();
            firstAccount.lock();
        }
    }

    @Override
    public void safeUnlockAccounts(Account firstAccount, Account secondAccount) {
        if (firstAccount.getId() < secondAccount.getId()) {
            secondAccount.unlock();
            firstAccount.unlock();

        } else {
            firstAccount.unlock();
            secondAccount.unlock();
        }
    }


}
