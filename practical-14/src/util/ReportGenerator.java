package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Walks the logs folder and builds the end-of-day summary.
 * Log line format: "HH:mm:ss TYPE ACCOUNT [TARGET] AMOUNT"   e.g.  "10:15:02 DEPOSIT AC0001 500"
 */
public class ReportGenerator {

    private ReportGenerator() { }

    public static String generate(Path logsDir, Path reportFile) throws IOException {
        List<Path> logs = new ArrayList<>();
        if (Files.isDirectory(logsDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir, "*.log")) {
                for (Path p : stream) {
                    logs.add(p);
                }
            }
        }
        Collections.sort(logs);

        long totalDeposits = 0;
        long totalWithdrawals = 0;
        int transfers = 0;
        int lineCount = 0;
        int skipped = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("===== END-OF-DAY SUMMARY =====\n");

        for (Path log : logs) {
            BasicFileAttributes attrs = Files.readAttributes(log, BasicFileAttributes.class);
            sb.append(String.format("File %s  size=%d bytes  modified=%s%n", log.getFileName(), attrs.size(),
                    attrs.lastModifiedTime().toInstant().truncatedTo(ChronoUnit.SECONDS)));

            try (BufferedReader reader = Files.newBufferedReader(log, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    String[] parts = line.trim().split("\\s+");
                    try {
                        long amount = Long.parseLong(parts[parts.length - 1]);
                        switch (parts[1]) {
                            case "DEPOSIT"  -> totalDeposits += amount;
                            case "WITHDRAW" -> totalWithdrawals += amount;
                            case "TRANSFER" -> transfers++;
                            default -> { }                      // e.g. OPEN lines are not counted
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        skipped++;                              // malformed line
                    }
                }
            }
        }

        sb.append(String.format("Log files read      : %d%n", logs.size()));
        sb.append(String.format("Lines read          : %d (skipped %d)%n", lineCount, skipped));
        sb.append(String.format("Total deposits      : Rs.%d%n", totalDeposits));
        sb.append(String.format("Total withdrawals   : Rs.%d%n", totalWithdrawals));
        sb.append(String.format("Transfers           : %d%n", transfers));
        sb.append(String.format("Net movement        : Rs.%d", totalDeposits - totalWithdrawals));

        String report = sb.toString();
        if (reportFile.getParent() != null) {
            Files.createDirectories(reportFile.getParent());
        }
        Files.write(reportFile, report.getBytes(StandardCharsets.UTF_8));
        return report;
    }
}
