package service;

import exception.BankException;
import model.Account;
import model.SavingsAccount;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/** Shows a classic lock-ordering deadlock and the fix (always lock the smaller accountNumber first). */
public class DeadlockDemo {

    /** BROKEN: locks 'from' first, then 'to' - opposite transfers lock in opposite order. */
    static void naiveTransfer(Account from, Account to, long amount) {
        synchronized (from) {
            pause(200);                         // makes the deadlock happen every time
            synchronized (to) {
                try {
                    from.withdraw(amount);
                    to.deposit(amount);
                } catch (BankException e) {
                    System.out.println("  " + e.getMessage());
                }
            }
        }
    }

    /** FIXED: the account with the smaller accountNumber is always locked first. */
    static void safeTransfer(Account from, Account to, long amount) {
        Account first  = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account second = (first == from) ? to : from;
        synchronized (first) {
            pause(200);
            synchronized (second) {
                try {
                    from.withdraw(amount);
                    to.deposit(amount);
                } catch (BankException e) {
                    System.out.println("  " + e.getMessage());
                }
            }
        }
    }

    private static void pause(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void run() throws InterruptedException {
        // ---- 1. the deadlock (uses its own pair: the stuck threads keep these locks forever) ----
        Account x = new SavingsAccount("Deadlock-X", 1000, 0);
        Account y = new SavingsAccount("Deadlock-Y", 1000, 0);
        Thread t1 = new Thread(() -> naiveTransfer(x, y, 100), "X-to-Y");
        Thread t2 = new Thread(() -> naiveTransfer(y, x, 50), "Y-to-X");
        t1.setDaemon(true);                     // daemon: the stuck threads must not block JVM exit
        t2.setDaemon(true);
        t1.start();
        t2.start();
        t1.join(1500);
        t2.join(1500);

        if (t1.isAlive() && t2.isAlive()) {
            ThreadMXBean mx = ManagementFactory.getThreadMXBean();
            long[] ids = mx.findDeadlockedThreads();
            int n = (ids == null) ? 0 : ids.length;
            System.out.println("  DEADLOCK: both threads are stuck (JVM reports " + n + " deadlocked threads)");
        }

        // ---- 2. the fix (a fresh pair, because x and y are still locked) ----
        Account a = new SavingsAccount("Safe-A", 1000, 0);
        Account b = new SavingsAccount("Safe-B", 1000, 0);
        Thread s1 = new Thread(() -> safeTransfer(a, b, 100), "A-to-B (safe)");
        Thread s2 = new Thread(() -> safeTransfer(b, a, 50), "B-to-A (safe)");
        s1.start();
        s2.start();
        s1.join();
        s2.join();
        System.out.println("  FIXED: both threads finished. " + a.getAccountNumber() + "="
                + a.getBalance() + ", " + b.getAccountNumber() + "=" + b.getBalance());
    }
}
