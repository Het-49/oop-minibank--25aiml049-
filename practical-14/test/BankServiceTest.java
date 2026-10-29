import exception.InsufficientFundsException;
import exception.ValidationException;
import model.Account;
import model.Customer;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import service.BankService;
import service.Repository;
import service.TransactionProcessor;
import util.StatePersister;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** JUnit 5 tests for MiniBank. Run with: mvn test   (or with the VS Code Test Runner). */
class BankServiceTest {

    @Test
    void depositIncreasesTheBalance() throws Exception {
        Account account = new SavingsAccount("Test User", 1000, 500);
        account.deposit(500);
        assertEquals(1500, account.getBalance());
    }

    @Test
    void withdrawalOverTheLimitThrowsAnException() {
        Account account = new SavingsAccount("Test User", 1000, 500);     // may keep at least Rs.500
        InsufficientFundsException e = assertThrows(InsufficientFundsException.class, () -> account.withdraw(800));
        assertEquals(300, e.getShortfall());
        assertEquals(1000, account.getBalance());                          // the balance did not change
    }

    @Test
    void manyConcurrentDepositsKeepTheBalanceCorrect() {
        Account account = new SavingsAccount("Test User", 0, 0);
        TransactionProcessor processor = new TransactionProcessor();
        for (int i = 0; i < 1000; i++) {
            processor.submit(() -> {
                try {
                    account.deposit(10);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            });
        }
        processor.stop();
        assertEquals(10000, account.getBalance());
    }

    @Test
    void serviceOpensDepositsAndWithdraws() throws Exception {
        BankService bank = new BankService(Files.createTempDirectory("minibank-test").resolve("logs"));
        Account a = bank.openAccount("Asha Patel", "9876543210", "asha@example.com", 1, 5000);
        assertEquals(6000, bank.deposit(a.getAccountNumber(), 1000));
        assertEquals(5500, bank.withdraw(a.getAccountNumber(), 500));
        assertTrue(bank.statement(a.getAccountNumber()).contains(a.getAccountNumber()));
    }

    @Test
    void serviceRejectsAnInvalidMobileNumber() throws Exception {
        BankService bank = new BankService(Files.createTempDirectory("minibank-test").resolve("logs"));
        assertThrows(ValidationException.class,
                () -> bank.openAccount("Asha Patel", "12345", "asha@example.com", 1, 1000));
    }

    @Test
    void repositoryFindsTheIdWithTheIdAnnotation() {
        Repository<Customer> repo = new Repository<>();
        Customer c = repo.save(new Customer("Test", "t@example.com", "9876543210"));
        assertEquals(c, repo.findById(c.getCustomerId()));
    }

    @Test
    void savedAccountsSurviveReloading() throws Exception {
        Path file = Files.createTempDirectory("minibank-test").resolve("accounts.dat");
        Account[] saved = { new SavingsAccount("Persist Me", 4200, 500) };
        StatePersister.save(saved, file);
        Account[] loaded = StatePersister.load(file);
        assertEquals(saved[0], loaded[0]);
        assertEquals(4200, loaded[0].getBalance());
    }
}
