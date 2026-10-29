package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;

/** Anything that can receive and release money. */
public interface Transactable {
    void deposit(long amount) throws InvalidAmountException;

    /** @return true when the withdrawal succeeded (failures are reported by exceptions) */
    boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException;
}
