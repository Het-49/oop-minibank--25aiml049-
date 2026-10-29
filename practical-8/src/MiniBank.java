import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;
import service.BankService;
import util.AuditSession;

public class MiniBank {

    public static void main(String[] args) {
        Account asha = new SavingsAccount("Asha", 3000, 500);
        Account ravi = new CurrentAccount("Ravi", 0, 1000);

        // 1. withdraw: try / catch / finally with friendly messages
        try {
            asha.withdraw(5000);
        } catch (InsufficientFundsException e) {
            System.out.println("Sorry: " + e.getMessage() + " (shortfall field = " + e.getShortfall() + ")");
        } catch (InvalidAmountException e) {
            System.out.println("Invalid amount: " + e.getMessage());
        } finally {
            System.out.println("finally: withdrawal attempt finished");
        }

        // 2. deposit of a non-positive amount
        try {
            asha.deposit(-10);
        } catch (InvalidAmountException e) {
            System.out.println("Invalid amount: " + e.getMessage());
        }

        // 3. transfer: success, then a failure that is re-thrown
        try {
            asha.transfer(ravi, 1000);
            System.out.println("transfer OK -> " + asha.getBalance() + " / " + ravi.getBalance());
            asha.transfer(ravi, 99999);
        } catch (BankException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }

        // 4. missing account
        BankService bank = new BankService();
        bank.add(asha);
        try {
            bank.find("AC9999");
        } catch (AccountNotFoundException e) {
            System.out.println("Lookup failed: " + e.getMessage());
        }

        // 5. try-with-resources with a small AutoCloseable class
        try (AuditSession session = new AuditSession("nightly-audit")) {
            session.log("checking " + asha.getAccountNumber());
        }
    }
}
