package model;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class of every MiniBank account.
 * The balance is private: it changes only through deposit(), withdraw() and transfer().
 */
public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account> {

    private static final long serialVersionUID = 1L;

    private static long accountCounter = 0;

    @Id
    private final String accountNumber;

    @MaxLength(30)
    private String ownerName;

    @Positive
    private long balance;          // whole rupees

    private boolean active;

    /** Main constructor: owner and opening balance. */
    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    /** Convenience constructor: opens the account with a zero balance. */
    public Account(String ownerName) {
        this(ownerName, 0);
    }

    /** After loading saved accounts the counter must continue after the highest saved number. */
    public static synchronized void ensureCounterAtLeast(long value) {
        if (value > accountCounter) {
            accountCounter = value;
        }
    }

    private static synchronized String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    // ---------- rules supplied by subclasses (run-time polymorphism) ----------
    @Override
    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    /** Lowest balance this account type may reach. Used to compute the shortfall. */
    protected long minimumAllowedBalance() {
        return 0;
    }

    protected long shortfall(long amount) {
        return Math.max(1, minimumAllowedBalance() - (balance - amount));
    }

    // ---------- the only ways the balance can change ----------
    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit must be positive, got " + amount);
        }
        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal must be positive, got " + amount);
        }
        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException(shortfall(amount));
        }
        balance -= amount;
        return true;
    }

    /**
     * Moves money to another account. Locks are always taken in accountNumber order,
     * so two opposite transfers can never deadlock.
     */
    public void transfer(Account to, long amount) throws BankException {
        boolean debited = false;
        try {
            if (to == null) {
                throw new AccountNotFoundException("(null)");
            }
            if (to == this) {
                throw new InvalidAmountException("Cannot transfer to the same account");
            }
            Account first = accountNumber.compareTo(to.accountNumber) < 0 ? this : to;
            Account second = (first == this) ? to : this;
            synchronized (first) {
                synchronized (second) {
                    withdraw(amount);
                    debited = true;
                    to.deposit(amount);
                }
            }
        } catch (BankException e) {
            if (debited) {              // credit failed after the debit: undo it
                synchronized (this) {
                    balance += amount;
                }
            }
            throw e;                    // re-throw so the caller can report it
        } finally {
            System.out.println("  [audit] transfer " + accountNumber + " -> "
                    + (to == null ? "?" : to.accountNumber) + " of Rs." + amount + " attempted");
        }
    }

    // ---------- getters (deliberately no setBalance) ----------
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName()     { return ownerName; }

    @Override
    public synchronized long getBalance() { return balance; }

    public boolean isActive()        { return active; }

    public void deactivate()         { this.active = false; }

    /** Natural order: by account number. */
    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }

    // ---------- Object contract ----------
    @Override
    public String toString() {
        return "Account[accountNumber=" + accountNumber + ", ownerName=" + ownerName
                + ", balance=" + getBalance() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Account other && accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
