package exception;

/** Thrown when an account number does not exist. */
public class AccountNotFoundException extends BankException {
    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber);
    }
}
