package service;

import model.Account;

import java.util.LinkedHashMap;
import java.util.Map;

/** Keeps the accounts of the bank in memory. */
public class BankService {
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    /** @return the account, or null when the number is unknown (exceptions come in Practical 8) */
    public Account find(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public long totalBalance() {
        long total = 0;
        for (Account a : accounts.values()) {
            total += a.getBalance();
        }
        return total;
    }
}
