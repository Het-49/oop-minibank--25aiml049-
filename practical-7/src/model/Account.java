package model;

import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

public abstract class Account implements Transactable, InterestBearing {
    private static long accountCounter = 0;

    @Id
    private final String accountNumber;
    @MaxLength(30)
    private String ownerName;
    @Positive
    private long balance;               // whole rupees
    private boolean active;

    /** Owner and opening balance. */
    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    /** Owner only: opens with a zero balance by calling the first constructor. */
    public Account(String ownerName) {
        this(ownerName, 0);
    }

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);     // AC0001, AC0002, ...
    }

    /** Interest rate in percent per year (each account type decides). */
    @Override
    public abstract double interestRate();

    /** Can this amount be taken out without breaking the account's rule? */
    public abstract boolean canWithdraw(long amount);

    @Override
    public void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    /** @return true if the money was withdrawn; false (balance unchanged) otherwise. */
    @Override
    public boolean withdraw(long amount) {
        if (amount > 0 && canWithdraw(amount)) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account[accountNumber=" + accountNumber + ", ownerName=" + ownerName
                + ", balance=" + balance + "]";
    }

    /** Two accounts are equal when their account numbers are equal. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Account other && accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }

    // getters only - there is deliberately NO setBalance()
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName()     { return ownerName; }
    public long getBalance()         { return balance; }
    public boolean isActive()        { return active; }
}
