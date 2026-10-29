package service;

import exception.BankException;
import model.Account;
import model.annotation.Audit;

import java.util.ArrayList;

/** What the menu may ask the bank to do. @Audit methods are recorded by the AuditLogger. */
public interface BankOperations {

    @Audit
    Account openAccount(String name, String mobile, String email, int type, long openingBalance) throws BankException;

    /** @return the new balance */
    @Audit
    long deposit(String accountNumber, long amount) throws BankException;

    /** @return the new balance */
    @Audit
    long withdraw(String accountNumber, long amount) throws BankException;

    @Audit
    void transfer(String fromAccount, String toAccount, long amount) throws BankException;

    String statement(String accountNumber) throws BankException;

    ArrayList<Account> listAccounts();
}
