package org.bank.service;

import org.bank.entity.Account;
import org.bank.exception.InsufficientFundsException;
import org.slf4j.Logger;

public class SimpleAccountService implements AccountService {
    private Logger logger;

    public SimpleAccountService(Logger logger) {
        this.logger = logger;
    }

    public void transfer(Account accountFrom, Account accountTo, long amount) throws InsufficientFundsException, InterruptedException {
        if (accountFrom.getReentrantLock().isLocked() || accountTo.getReentrantLock().isLocked()){
            logger.warn("SOMETHING LOCKED");
        }
        logger.info("TRANSACTION FROM {} TO {}, AMOUNT:{}",accountFrom.getId(),accountTo.getId(),amount);
        accountFrom.getReentrantLock().lock();
        accountTo.getReentrantLock().lock();
        if (accountFrom.getReentrantLock().isLocked() || accountTo.getReentrantLock().isLocked()){
            logger.warn("LOOOOOCK AFTER METHOD");
        }
        if (accountFrom.getBalance() < amount) {
            accountFrom.getReentrantLock().unlock();
            accountTo.getReentrantLock().unlock();
            throw new InsufficientFundsException("Incorrect amount");
        }
        logger.debug("(from) ID: {} old balance is {} ",accountFrom.getId(),accountFrom.getBalance());
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);
        logger.debug("(from) ID: {} NEW balance is {} ",accountFrom.getId(),accountFrom.getBalance());

        accountFrom.getReentrantLock().unlock();
        accountTo.getReentrantLock().unlock();
    }
}
