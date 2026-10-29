package model;

public class PremiumSavingsAccount extends SavingsAccount implements Premium {
    private static final long serialVersionUID = 1L;


    public PremiumSavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance, minBalance);
    }

    @Override
    public double interestRate() {
        return 5.5;
    }
}
