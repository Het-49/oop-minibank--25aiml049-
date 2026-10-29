package model;

/** Functional interface: exactly one abstract method. */
public interface WithdrawRule {
    boolean allow(Account account, long amount);
}
