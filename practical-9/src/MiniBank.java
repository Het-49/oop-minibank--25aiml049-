import exception.BankException;
import model.Account;
import model.SavingsAccount;
import service.DeadlockDemo;
import service.ProducerConsumerDemo;
import service.TransactionProcessor;

public class MiniBank {

    public static void main(String[] args) throws InterruptedException {
        // 1 + 2. thread pool: submit many deposit / withdrawal tasks, then stop and wait
        Account shared = new SavingsAccount("Pool-Test", 10000, 0);
        TransactionProcessor processor = new TransactionProcessor();
        for (int i = 0; i < 100; i++) {
            processor.submit(() -> {
                try {
                    shared.deposit(100);
                } catch (BankException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
        for (int i = 0; i < 50; i++) {
            processor.submit(() -> {
                try {
                    shared.withdraw(100);
                } catch (BankException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
        processor.stop();
        System.out.println("thread pool: 100 deposits + 50 withdrawals of 100 -> balance " + shared.getBalance()
                + " (expected 15000)");

        // 3. producer-consumer with wait() / notify()
        System.out.println("---- producer / consumer ----");
        ProducerConsumerDemo.run(new SavingsAccount("PC-Test", 500, 0));

        // 4. deadlock, then the lock-ordering fix
        System.out.println("---- deadlock and fix ----");
        DeadlockDemo.run();
    }
}
