package service;

import exception.BankException;
import model.Account;
import model.Command;
import model.TransactionType;

/** One producer thread and one consumer thread share a small array buffer (wait/notify). */
public class ProducerConsumerDemo {

    /** Bounded buffer backed by a plain array. */
    static class TransactionBuffer {
        private final Command[] items = new Command[3];
        private int count = 0, putIndex = 0, takeIndex = 0;

        public synchronized void put(Command c) throws InterruptedException {
            while (count == items.length) {
                wait();                     // buffer full: wait for the consumer
            }
            items[putIndex] = c;
            putIndex = (putIndex + 1) % items.length;
            count++;
            notify();                       // wake the consumer
        }

        public synchronized Command take() throws InterruptedException {
            while (count == 0) {
                wait();                     // buffer empty: wait for the producer
            }
            Command c = items[takeIndex];
            takeIndex = (takeIndex + 1) % items.length;
            count--;
            notify();                       // wake the producer
            return c;
        }
    }

    private static final Command POISON = new Command(TransactionType.DEPOSIT, "END", 0);

    public static void run(Account account) throws InterruptedException {
        TransactionBuffer buffer = new TransactionBuffer();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 6; i++) {
                    TransactionType type = (i % 2 == 0) ? TransactionType.WITHDRAW : TransactionType.DEPOSIT;
                    Command c = new Command(type, account.getAccountNumber(), i * 100L);
                    buffer.put(c);
                    System.out.println("  produced: " + c);
                }
                buffer.put(POISON);         // tells the consumer to stop
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "producer");

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Command c = buffer.take();
                    if (c == POISON) break;
                    try {
                        if (c.type() == TransactionType.DEPOSIT) account.deposit(c.amount());
                        else account.withdraw(c.amount());
                        System.out.println("  consumed: " + c + "  -> balance " + account.getBalance());
                    } catch (BankException e) {
                        System.out.println("  consumed: " + c + "  -> rejected: " + e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
