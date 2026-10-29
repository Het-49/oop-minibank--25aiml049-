public class Account {
    private static long accountCounter = 0;

    private final String accountNumber;
    private String ownerName;
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

    public void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    /** @return true if the money was withdrawn; false (balance unchanged) otherwise. */
    public boolean withdraw(long amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    // getters only - there is deliberately NO setBalance()
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName()     { return ownerName; }
    public long getBalance()         { return balance; }
    public boolean isActive()        { return active; }
}
