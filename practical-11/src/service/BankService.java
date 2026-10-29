package service;

import exception.AccountNotFoundException;
import model.Account;

import java.util.LinkedHashMap;
import java.util.Map;

public class BankService {
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    /** Now an unknown number is reported with an exception instead of null. */
    public Account find(String accountNumber) throws AccountNotFoundException {
        Account a = accounts.get(accountNumber);
        if (a == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return a;
    }
}
