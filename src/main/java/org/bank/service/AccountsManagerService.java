package org.bank.service;

import org.bank.entity.Account;
import org.bank.exception.AccountDeserializeException;

import java.util.List;

public interface AccountsManagerService {
    List<Account> getAllAccounts() throws AccountDeserializeException;

    void createAccountFiles(int count);
}
