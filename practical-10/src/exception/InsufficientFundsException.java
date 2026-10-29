package exception;

/** Thrown when a withdrawal would break the account's balance rule. */
public class InsufficientFundsException extends BankException {
    private static final long serialVersionUID = 1L;

    private final long shortfall;

    public InsufficientFundsException(long shortfall) {
        super("Insufficient funds: short by Rs." + shortfall);
        this.shortfall = shortfall;
    }

    public long getShortfall() {
        return shortfall;
    }
}
