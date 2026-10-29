package model;

public class FixedDepositAccount extends Account {

    public FixedDepositAccount(String ownerName, long depositAmount) {
        super(ownerName, depositAmount);
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return false;                       // the deposit is locked
    }

    @Override
    protected long shortfall(long amount) {
        return amount;                      // nothing at all can be taken out
    }
}
