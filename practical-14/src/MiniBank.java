import exception.BankException;
import model.Account;
import model.BankInfo;
import model.MenuOption;
import service.BankOperations;
import service.BankService;
import util.AuditLogger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * MiniBank - the finished console banking application.
 *   java -jar MiniBank.jar            start the menu
 *   java -jar MiniBank.jar --echo     same, but also prints every typed line (used for the report)
 */
public class MiniBank {
    private static final Path DATA = Path.of("data");
    private static boolean echo = false;

    public static void main(String[] args) throws Exception {
        echo = args.length > 0 && args[0].equals("--echo");

        BankService service = new BankService(DATA.resolve("logs"));
        AuditLogger audit = new AuditLogger();
        BankOperations bank = audit.wrap(service, BankOperations.class);     // calls are recorded reflectively

        int loaded = service.load(DATA.resolve("accounts.dat"));             // Practical 11: load on start-up
        BankInfo info = new BankInfo("MiniBank", "Vadodara Main Branch");
        System.out.println("=======================================");
        System.out.println("  " + info);
        System.out.println("=======================================");
        System.out.println("Loaded " + loaded + " saved account(s).");

        Scanner in = new Scanner(System.in);
        MenuOption[] options = MenuOption.values();
        boolean running = true;
        while (running) {
            System.out.println();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.print("Choose an option: ");
            if (!in.hasNextLine()) {
                break;
            }
            MenuOption option;
            try {
                option = options[Integer.parseInt(readLine(in).trim()) - 1];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Please enter a number from 1 to " + options.length + ".");
                continue;
            }

            try {
                running = switch (option) {
                    case OPEN_ACCOUNT -> { openAccount(in, bank); yield true; }
                    case DEPOSIT -> {
                        System.out.println("New balance: Rs." + bank.deposit(ask(in, "Account number: "), askAmount(in)));
                        yield true;
                    }
                    case WITHDRAW -> {
                        System.out.println("New balance: Rs." + bank.withdraw(ask(in, "Account number: "), askAmount(in)));
                        yield true;
                    }
                    case TRANSFER -> {
                        String from = ask(in, "From account: ");
                        String to = ask(in, "To account: ");
                        bank.transfer(from, to, askAmount(in));
                        System.out.println("Transfer complete.");
                        yield true;
                    }
                    case STATEMENT -> {
                        System.out.println(bank.statement(ask(in, "Account number: ")));
                        yield true;
                    }
                    case LIST_ACCOUNTS -> { listAccounts(bank); yield true; }
                    case BATCH -> { batch(in, service); yield true; }
                    case REPORT -> {
                        System.out.println(service.report());
                        System.out.println("---- audit trail (@Audit methods) ----");
                        audit.getTrail().forEach(t -> System.out.println("  " + t));
                        yield true;
                    }
                    case EXIT -> false;
                };
            } catch (BankException e) {
                System.out.println("Failed: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }

        service.save(DATA.resolve("accounts.dat"));                          // Practical 11: save on exit
        System.out.println("Accounts saved. Goodbye! Thank you for using MiniBank.");
    }

    private static String readLine(Scanner in) {
        String line = in.nextLine();
        if (echo) {
            System.out.println(line);
        }
        return line;
    }

    private static String ask(Scanner in, String prompt) {
        System.out.print(prompt);
        return readLine(in).trim();
    }

    private static long askAmount(Scanner in) {
        return Long.parseLong(ask(in, "Amount (Rs.): "));
    }

    private static void openAccount(Scanner in, BankOperations bank) throws BankException {
        String name = ask(in, "Customer name: ");
        String mobile = ask(in, "Mobile (10 digits): ");
        String email = ask(in, "Email: ");
        int type = Integer.parseInt(ask(in, "Account type (1=Savings, 2=Current, 3=Fixed Deposit): "));
        long opening = Long.parseLong(ask(in, "Opening amount (Rs.): "));
        Account a = bank.openAccount(name, mobile, email, type, opening);
        System.out.println("Opened " + a.getClass().getSimpleName() + " " + a.getAccountNumber()
                + " for " + a.getOwnerName() + ", balance Rs." + a.getBalance());
    }

    private static void listAccounts(BankOperations bank) {
        ArrayList<Account> list = bank.listAccounts();
        if (list.isEmpty()) {
            System.out.println("No accounts yet.");
            return;
        }
        for (Account a : list) {
            System.out.printf("%s  %-20s %-14s Rs.%d%n", a.getAccountNumber(), a.getClass().getSimpleName(),
                    a.getOwnerName(), a.getBalance());
        }
    }

    /** Safe batch processing: 100 deposits and 40 withdrawals of Rs.10 on four threads. */
    private static void batch(Scanner in, BankService service) throws BankException {
        String acc = ask(in, "Account number for the batch: ").toUpperCase();
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            lines.add("DEPOSIT " + acc + " 10");
        }
        for (int i = 0; i < 40; i++) {
            lines.add("WITHDRAW " + acc + " 10");
        }
        System.out.println(service.processBatch(lines));
        System.out.println("Expected change: +Rs.600 (100 x 10 - 40 x 10)");
    }
}
