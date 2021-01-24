package org.bank.dao;

import org.bank.entity.Account;
import org.bank.exception.AccountDeserializeException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AccountDao {
    private Path accountsFolder;
    public Path getAccountsFolder() {
        return accountsFolder;
    }

    public AccountDao(Path accountsFolder) {
        this.accountsFolder = accountsFolder;
    }

    public Account getAccountById(int id) throws AccountDeserializeException {
        Path pathToAccountFile = Paths.get(accountsFolder.toString(), String.valueOf(id));

        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(pathToAccountFile.toFile()))
        ) {
            return (Account) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        throw new AccountDeserializeException("Account can not be deserialized");
    }

    public void saveAccountData(Account account) {
        Path pathToAccountFile = Paths.get(accountsFolder.toString(), String.valueOf(account.getId()));

        try (
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(pathToAccountFile.toFile()))
        ) {
            objectOutputStream.writeObject(account);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
