package exception;

/** Thrown when user input (name, mobile, email, account type ...) is not valid. */
public class ValidationException extends BankException {
    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }
}
