package service;

import exception.AccountNotFoundException;
import model.Account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/** Keeps the accounts in a HashMap keyed by account number, plus a transaction history list. */
public class AccountStore {
    private final HashMap<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> history = new ArrayList<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
        history.add("ADDED " + account.getAccountNumber() + " (" + account.getOwnerName() + ")");
    }

    public Account get(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public void record(String entry) {
        history.add(entry);
    }

    public ArrayList<String> getHistory() {
        return new ArrayList<>(history);
    }

    /** Lowest balance first, using a Comparator. */
    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = getAll();
        list.sort(Comparator.comparingLong(Account::getBalance));
        return list;
    }

    /** Natural order, i.e. Account.compareTo(): by account number. */
    public ArrayList<Account> sortedByNumber() {
        ArrayList<Account> list = getAll();
        Collections.sort(list);
        return list;
    }
}
