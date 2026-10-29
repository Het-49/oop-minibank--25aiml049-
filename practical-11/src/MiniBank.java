import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.ReportGenerator;
import util.StatePersister;
import util.TransactionLog;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class MiniBank {

    public static void main(String[] args) throws Exception {
        Path dataDir = Path.of("data");
        Path logsDir = dataDir.resolve("logs");
        Path file = dataDir.resolve("accounts.dat");
        deleteOldLogs(logsDir);                     // so the demo can be run again and again

        // 1. save the accounts
        Account[] accounts = {
            new SavingsAccount("Asha Patel", 5000, 500),
            new CurrentAccount("Ravi Shah", 1000, 2000),
            new FixedDepositAccount("Meena Joshi", 20000)
        };
        accounts[0].deposit(1500);
        accounts[1].withdraw(2500);
        StatePersister.save(accounts, file);
        System.out.println("Saved " + accounts.length + " accounts to " + file + " (" + Files.size(file) + " bytes)");

        // 2. reload them into a new array and compare
        Account[] loaded = StatePersister.load(file);
        System.out.println("Reloaded " + loaded.length + " accounts:");
        for (int i = 0; i < loaded.length; i++) {
            System.out.println("  " + loaded[i] + "  equals original: " + loaded[i].equals(accounts[i]));
        }

        // 3. append-only transaction logs (two days)
        Path day1 = logsDir.resolve("2026-10-27.log");
        Path day2 = logsDir.resolve("2026-10-28.log");
        TransactionLog.append(day1, "09:15:02 DEPOSIT AC0001 1500");
        TransactionLog.append(day1, "11:40:10 WITHDRAW AC0002 2500");
        TransactionLog.append(day1, "14:05:33 TRANSFER AC0001 AC0002 700");
        TransactionLog.append(day2, "10:01:45 DEPOSIT AC0003 4000");
        TransactionLog.append(day2, "12:30:00 WITHDRAW AC0001 600");
        TransactionLog.append(day2, "this line is damaged");
        TransactionLog.append(day2, "15:45:12 DEPOSIT AC0002 250");
        System.out.println("Logs written to " + logsDir);

        // 4. end-of-day report
        String report = ReportGenerator.generate(logsDir, dataDir.resolve("report.txt"));
        System.out.println(report);
        System.out.println("Report saved to " + dataDir.resolve("report.txt"));
    }

    private static void deleteOldLogs(Path logsDir) throws IOException {
        if (Files.isDirectory(logsDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log")) {
                for (Path p : stream) {
                    Files.delete(p);
                }
            }
        }
    }
}
