package org.bank.service;

import org.bank.dao.AccountDao;
import org.bank.entity.Account;
import org.bank.exception.AccountDeserializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleAccountsManagerService implements AccountsManagerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAccountsManagerService.class);
    private final AccountDao accountDao;

    public SimpleAccountsManagerService(AccountDao accountDao) {
        this.accountDao = accountDao;
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
            LOGGER.info("User id:{} username:{} balance:{}", account.getId(), account.getUsername(), account.getBalance());
            summaryBalance += account.getBalance();

        } while (Files.exists(pathToNextAccount));
        LOGGER.info("SUMMARY BALANCE: {}", summaryBalance);

        return accountList;
    }

    @Override
    public void createAccountFiles(int count) {
        for (int i = 0; i < count; i++) {
            Account account = new Account();
            account.setId(i);
            account.setUsername("USER");
            account.setBalance((long) ThreadLocalRandom.current().nextInt(10_000));

            accountDao.saveAccountData(account);
        }
    }

    @Override
    public void saveAllAccounts(List<Account> accountList) {
        accountList.forEach(accountDao::saveAccountData);
    }

}
