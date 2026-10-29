public class MiniBank {

    public static void main(String[] args) {
        System.out.println(new BankInfo("MiniBank", "Vadodara Main Branch"));

        Customer c1 = new Customer("Asha Patel", "asha@example.com", "9876543210");
        Customer c2 = new Customer("Ravi Shah", "ravi@example.com", "9123456780");
        System.out.println(c1.getCustomerId() + " " + c1.getName() + " | " + c2.getCustomerId() + " " + c2.getName());

        Account[] accounts = {
            new Account("Asha Patel", 5000),
            new Account("Ravi Shah"),               // zero balance
            new Account("Meena Joshi", 20000)
        };

        accounts[0].deposit(1000);
        boolean ok1 = accounts[0].withdraw(2500);
        accounts[1].deposit(500);
        boolean ok2 = accounts[1].withdraw(4000);   // too much: refused
        boolean ok3 = accounts[2].withdraw(20000);

        System.out.println("withdraw 2500 from AC0001 -> " + ok1);
        System.out.println("withdraw 4000 from AC0002 -> " + ok2);
        System.out.println("withdraw 20000 from AC0003 -> " + ok3);

        for (Account a : accounts) {
            System.out.println(a.getAccountNumber() + " " + a.getOwnerName() + " balance = " + a.getBalance());
        }
    }
}
