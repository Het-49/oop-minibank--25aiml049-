public class FixedDepositAccount extends Account {

    public FixedDepositAccount(String ownerName, long depositAmount) {
        super(ownerName, depositAmount);
    }

    public double interestRate() {
        return 7.0;
    }

    /** The deposit is locked. */
    public boolean canWithdraw(long amount) {
        return false;
    }
}
