package model;

/** Accounts that earn interest. */
public interface InterestBearing {
    double interestRate();

    long getBalance();

    /** Default method: simple yearly interest = balance x rate / 100. */
    default double yearlyInterest() {
        return getBalance() * interestRate() / 100.0;
    }
}
