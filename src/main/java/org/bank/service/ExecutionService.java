package org.bank.service;

import org.bank.UserThread;
import org.bank.dao.AccountDao;
import org.bank.entity.Account;
import org.bank.exception.AccountDeserializeException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutionService {
    private static final int NUMBER_OF_THREAD = 20;
    private static final int NUMBER_OF_OPERATIONS_IN_EACH_THREAD = 50;
    private static final int TOTAL_COUNT_OF_ACCOUNTS = 10;
    private static final Path PATH_TO_ACCOUNTS_DIR = Paths.get("src/main/resources/accounts");
    private static final AccountDao ACCOUNT_DAO = new AccountDao(PATH_TO_ACCOUNTS_DIR);
    private final AccountService accountService = new SimpleAccountService(ACCOUNT_DAO);
    private final AccountsManagerService accountsManagerService = new SimpleAccountsManagerService(ACCOUNT_DAO);
    private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREAD);


    public void startThreads() {
        List<Account> accounts = new ArrayList<>();
        accountsManagerService.createAccountFiles(TOTAL_COUNT_OF_ACCOUNTS);

        try {
            accounts = accountsManagerService.getAllAccounts();
        } catch (AccountDeserializeException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < NUMBER_OF_THREAD; i++) {
            UserThread userThread = new UserThread("user thread " + i, accountService, accounts);
            userThread.setOperationsInEachThread(NUMBER_OF_OPERATIONS_IN_EACH_THREAD);
            executorService.submit(userThread);
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
            accountsManagerService.saveAllAccounts(accounts);
            accountsManagerService.getAllAccounts();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (AccountDeserializeException e) {
            e.printStackTrace();
        }
    }

}
