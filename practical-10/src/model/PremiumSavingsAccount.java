package model;

public class PremiumSavingsAccount extends SavingsAccount implements Premium {

    public PremiumSavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance, minBalance);
    }

    @Override
    public double interestRate() {
        return 5.5;
    }
}
