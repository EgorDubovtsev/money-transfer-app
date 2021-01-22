package org.bank.service;

import org.bank.UserThread;
import org.bank.dao.AccountDao;
import org.bank.exception.AccountDeserializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExecutionService {
    private static final int NUMBER_OF_THREAD = 20;
    private static final int NUMBER_OF_OPERATIONS_IN_EACH_THREAD = 50;
    private static final int TOTAL_COUNT_OF_ACCOUNTS = 10;
    private List<UserThread> threads = new ArrayList<>();

    public void startThreads() {
        Logger logger = LoggerFactory.getLogger(ExecutionService.class);
        AccountService accountService = new SimpleAccountService(logger);
        Random random = new Random();
        Path pathToAccountsDir = Paths.get("src/main/resources/accounts");
        AccountDao accountDao = new AccountDao(pathToAccountsDir);

        AccountsManagerService accountsManagerService = new SimpleAccountsManagerService(logger, accountDao, random);
        accountsManagerService.createAccountFiles(TOTAL_COUNT_OF_ACCOUNTS);

        try {
            accountsManagerService.getAllAccounts();
        } catch (AccountDeserializeException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < NUMBER_OF_THREAD; i++) {
            UserThread userThread = new UserThread("user thread " + i,
                    accountService, TOTAL_COUNT_OF_ACCOUNTS - 1,
                    random, accountDao);
            threads.add(userThread);
            userThread.setOperationsInEachThread(NUMBER_OF_OPERATIONS_IN_EACH_THREAD);
            userThread.start();
        }

        try {
            accountsManagerService.getAllAccounts();        //todo: call result operation in the end

        } catch (AccountDeserializeException e) {
            e.printStackTrace();
        }
    }

}