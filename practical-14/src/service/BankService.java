package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import exception.ValidationException;
import model.Account;
import model.Command;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.CommandParser;
import util.ReportGenerator;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;
import util.Validator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The heart of MiniBank. It wires together the Repository, the Validator, the exceptions,
 * the transaction log, the TransactionProcessor and the report.
 */
public class BankService implements BankOperations {
    public static final long SAVINGS_MIN_BALANCE = 500;
    public static final long CURRENT_OVERDRAFT = 5000;
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Repository<Account> accounts = new Repository<>();
    private final Repository<Customer> customers = new Repository<>();
    private final Path logsDir;

    public BankService(Path logsDir) {
        this.logsDir = logsDir;
    }

    // ---------- persistence (Practical 11) ----------
    /** Loads the saved accounts, if the file exists. @return how many accounts were loaded */
    public int load(Path file) throws IOException, ClassNotFoundException {
        if (!Files.exists(file)) {
            return 0;
        }
        Account[] saved = StatePersister.load(file);
        accounts.saveAll(saved);
        return saved.length;
    }

    public void save(Path file) throws IOException {
        StatePersister.save(accounts.findAll().toArray(new Account[0]), file);
    }

    // ---------- operations ----------
    @Override
    public Account openAccount(String name, String mobile, String email, int type, long openingBalance)
            throws BankException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name must not be empty");
        }
        if (!Validator.isValidMobile(mobile)) {
            throw new ValidationException("Invalid mobile number (10 digits starting with 6-9)");
        }
        if (!Validator.isValidEmail(email)) {
            throw new ValidationException("Invalid email address");
        }
        if (openingBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }
        Customer customer = new Customer(name.trim(), email, mobile);
        Account account = switch (type) {
            case 1 -> new SavingsAccount(customer.getName(), openingBalance, SAVINGS_MIN_BALANCE);
            case 2 -> new CurrentAccount(customer.getName(), openingBalance, CURRENT_OVERDRAFT);
            case 3 -> new FixedDepositAccount(customer.getName(), openingBalance);
            default -> throw new ValidationException("Account type must be 1, 2 or 3");
        };
        customers.save(customer);
        accounts.save(account);
        log("OPEN", account.getAccountNumber(), openingBalance);
        return account;
    }

    @Override
    public long deposit(String accountNumber, long amount) throws BankException {
        Account account = find(accountNumber);
        account.deposit(amount);
        log("DEPOSIT", account.getAccountNumber(), amount);
        return account.getBalance();
    }

    @Override
    public long withdraw(String accountNumber, long amount) throws BankException {
        Account account = find(accountNumber);
        account.withdraw(amount);
        log("WITHDRAW", account.getAccountNumber(), amount);
        return account.getBalance();
    }

    @Override
    public void transfer(String fromAccount, String toAccount, long amount) throws BankException {
        Account from = find(fromAccount);
        Account to = find(toAccount);
        from.transfer(to, amount);
        log("TRANSFER", from.getAccountNumber() + " " + to.getAccountNumber(), amount);
    }

    @Override
    public String statement(String accountNumber) throws BankException {
        return StatementFormatter.buildStatement(find(accountNumber));
    }

    @Override
    public ArrayList<Account> listAccounts() {
        ArrayList<Account> list = accounts.findAll();
        Collections.sort(list);                     // natural order: by account number
        return list;
    }

    // ---------- batch processing (Practical 10 + 12) ----------
    /** Runs lines such as "DEPOSIT AC0001 10" on the thread pool. @return a one-line summary */
    public String processBatch(List<String> commandLines) throws BankException {
        ArrayList<Command> commands = new ArrayList<>();
        for (String line : commandLines) {
            try {
                commands.add(CommandParser.parse(line));
            } catch (IllegalArgumentException e) {
                throw new ValidationException(e.getMessage());
            }
        }
        TransactionProcessor processor = new TransactionProcessor();
        for (Account a : accounts.findAll()) {
            processor.register(a);
        }
        processor.onSuccess(c -> log(c.type().name(), c.accountNumber(), c.amount()));
        commands.forEach(processor::submitCommand);
        processor.stop();                           // waits until every task is finished
        return "Batch finished: " + processor.getProcessed() + " processed, " + processor.getFailed() + " failed";
    }

    // ---------- report (Practical 11) ----------
    public String report() throws IOException {
        return ReportGenerator.generate(logsDir, logsDir.resolveSibling("report.txt"));
    }

    // ---------- helpers ----------
    private Account find(String accountNumber) throws AccountNotFoundException {
        Account account = accountNumber == null ? null : accounts.findById(accountNumber.trim().toUpperCase());
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    private void log(String type, String accountPart, long amount) {
        Path file = logsDir.resolve(LocalDate.now() + ".log");
        try {
            TransactionLog.append(file, LocalTime.now().format(TIME) + " " + type + " " + accountPart + " " + amount);
        } catch (IOException e) {
            System.err.println("Warning: could not write the transaction log: " + e.getMessage());
        }
    }
}
