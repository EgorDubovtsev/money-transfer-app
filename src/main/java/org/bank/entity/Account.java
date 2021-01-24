package org.bank.entity;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

public class Account implements Serializable {
    private int id;
    private String username;
    private Long balance;
    private final ReentrantLock reentrantLock = new ReentrantLock(true);


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
    public void lock(){
        reentrantLock.lock();
    }
    public void unlock(){
        reentrantLock.unlock();
    }
    public ReentrantLock getReentrantLock() {
        return reentrantLock;
    }
}
