package util;

import model.Command;
import model.TransactionType;

public class CommandParser {

    public static Command parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Command is empty");
        }
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Expected: <TYPE> <ACCOUNT> <AMOUNT>, got: " + line);
        }
        try {
            TransactionType type = TransactionType.valueOf(parts[0].toUpperCase());
            long amount = Long.parseLong(parts[2]);
            return new Command(type, parts[1].toUpperCase(), amount);
        } catch (IllegalArgumentException e) {          // also covers NumberFormatException
            throw new IllegalArgumentException("Bad command '" + line + "': " + e.getMessage());
        }
    }
}
