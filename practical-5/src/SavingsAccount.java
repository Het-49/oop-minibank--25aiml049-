public class SavingsAccount extends Account {
    private long minBalance;

    public SavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance);       // initialise the inherited fields
        this.minBalance = minBalance;
    }

    public long getMinBalance() { return minBalance; }

    public double interestRate() {
        return 4.0;
    }

    /** Allowed only if the balance stays at or above minBalance. */
    public boolean canWithdraw(long amount) {
        return getBalance() - amount >= minBalance;
    }
}
