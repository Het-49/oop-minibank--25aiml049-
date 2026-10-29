package exception;

/** Checked base class for every business-rule failure in MiniBank. */
public class BankException extends Exception {
    private static final long serialVersionUID = 1L;

    public BankException(String message) {
        super(message);
    }
}
