package org.bank.service;

import org.bank.dao.AccountDao;
import org.bank.entity.Account;
import org.bank.exception.AccountDeserializeException;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleAccountsManagerService implements AccountsManagerService {
    private Logger logger;
    private AccountDao accountDao;
    private Random random;

    public SimpleAccountsManagerService(Logger logger, AccountDao accountDao, Random random) {
        this.logger = logger;
        this.accountDao = accountDao;
        this.random = random;
    }

    @Override
    public List<Account> getAllAccounts() throws AccountDeserializeException {
        List<Account> accountList = new ArrayList<>();
        int i = 0;
        Path pathToNextAccount;
        Long summaryBalance = 0L;

        do {
            Account account = accountDao.getAccountById(i);
            accountList.add(account);
            pathToNextAccount = Paths.get(accountDao.getAccountsFolder().toString(), String.valueOf(++i));
            logger.info("User id:{} username:{} balance:{}", account.getId(), account.getUsername(), account.getBalance());
            summaryBalance += account.getBalance();

        } while (Files.exists(pathToNextAccount));
        logger.info("SUMMARY BALANCE: {}", summaryBalance);

        return accountList;
    }

    @Override
    public void createAccountFiles(int count) {
        for (int i = 0; i < count; i++) {
            Account account = new Account();
            account.setId(i);
            account.setUsername("USER");
            account.setBalance((long) random.nextInt(10000));

            accountDao.saveAccountData(account);
        }
    }

    @Override
    public void saveAllAccounts(List<Account> accountList) {
        accountList.forEach(account -> accountDao.saveAccountData(account));
    }

}
