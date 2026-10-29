import exception.AccountNotFoundException;
import model.Account;
import model.Command;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import service.AccountStore;
import service.TransactionProcessor;
import util.CommandParser;

public class MiniBank {

    public static void main(String[] args) throws Exception {
        // 1. HashMap store + history list
        AccountStore store = new AccountStore();
        store.add(new SavingsAccount("Asha Patel", 12000, 500));
        store.add(new CurrentAccount("Ravi Shah", 3500, 2000));
        store.add(new FixedDepositAccount("Meena Joshi", 50000));
        store.add(new SavingsAccount("Kiran Desai", 800, 500));
        store.add(new SavingsAccount("Neha Mehta", 7600, 500));
        store.record("NOTE sample accounts created");

        System.out.println("get(\"AC0002\") -> " + store.get("AC0002"));
        try {
            store.get("AC9999");
        } catch (AccountNotFoundException e) {
            System.out.println("get(\"AC9999\") -> " + e.getMessage());
        }

        // 2. sorting: Comparator (by balance) and Comparable (by account number)
        System.out.println("---- sorted by balance (low to high) ----");
        for (Account a : store.sortedByBalance()) {
            System.out.println("  " + a.getBalance() + "  " + a);
        }
        System.out.println("---- sorted by account number (compareTo) ----");
        for (Account a : store.sortedByNumber()) {
            System.out.println("  " + a.getAccountNumber() + "  " + a.getOwnerName());
        }
        System.out.println("---- history ----");
        store.getHistory().forEach(h -> System.out.println("  " + h));

        // 3. TransactionProcessor over a ConcurrentHashMap
        TransactionProcessor processor = new TransactionProcessor();
        for (Account a : store.getAll()) {
            processor.register(a);
        }
        for (int i = 0; i < 40; i++) {
            processor.submitCommand(CommandParser.parse("DEPOSIT AC0001 100"));
        }
        for (int i = 0; i < 10; i++) {
            processor.submitCommand(CommandParser.parse("WITHDRAW AC0001 100"));
        }
        processor.submitCommand(CommandParser.parse("DEPOSIT AC9999 100"));   // unknown account: counted as failed
        processor.stop();
        System.out.println("---- thread-safe processing ----");
        System.out.println("AC0001 balance = " + processor.get("AC0001").getBalance() + " (expected 15000)");
        System.out.println("processed = " + processor.getProcessed() + ", failed = " + processor.getFailed());
    }
}
