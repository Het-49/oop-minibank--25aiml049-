package model;

/** Anything that can receive and release money. */
public interface Transactable {
    void deposit(long amount);

    boolean withdraw(long amount);
}
