package service;

import exception.BankException;
import model.Account;
import model.Command;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/** Runs transaction tasks on a managed pool of four worker threads over a thread-safe account map. */
public class TransactionProcessor {
    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final AtomicInteger processed = new AtomicInteger();
    private final AtomicInteger failed = new AtomicInteger();
    private volatile Consumer<Command> onSuccess = c -> { };

    public void register(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    /** Called after every successful command (the bank uses it to write the transaction log). */
    public void onSuccess(Consumer<Command> listener) {
        this.onSuccess = listener;
    }

    public void submit(Runnable task) {
        pool.execute(task);
    }

    /** Submits a parsed DEPOSIT or WITHDRAW command. */
    public void submitCommand(Command command) {
        pool.execute(() -> apply(command));
    }

    private void apply(Command c) {
        Account account = accounts.get(c.accountNumber());
        try {
            if (account == null) {
                throw new IllegalArgumentException("unknown account " + c.accountNumber());
            }
            switch (c.type()) {
                case DEPOSIT  -> account.deposit(c.amount());
                case WITHDRAW -> account.withdraw(c.amount());
                case TRANSFER -> throw new IllegalArgumentException("transfers are not batch-processed");
            }
            processed.incrementAndGet();
            onSuccess.accept(c);
        } catch (BankException | IllegalArgumentException e) {
            failed.incrementAndGet();
        }
    }

    public int getProcessed() { return processed.get(); }
    public int getFailed()    { return failed.get(); }

    /** Stops accepting tasks, then waits for the running ones to finish. */
    public void stop() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
