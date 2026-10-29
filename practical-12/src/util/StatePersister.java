package util;

import model.Account;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/** Saves and loads all accounts with Java serialization. */
public class StatePersister {

    private StatePersister() { }

    public static void save(Account[] accounts, Path file) throws IOException {
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(accounts);
        }
    }

    public static Account[] load(Path file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            Account[] accounts = (Account[]) in.readObject();
            // static counters are not serialized: continue after the highest saved number
            long highest = 0;
            for (Account a : accounts) {
                highest = Math.max(highest, Long.parseLong(a.getAccountNumber().substring(2)));
            }
            Account.ensureCounterAtLeast(highest);
            return accounts;
        }
    }
}
