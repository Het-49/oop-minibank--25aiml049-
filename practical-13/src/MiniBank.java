import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import service.Repository;

public class MiniBank {

    public static void main(String[] args) {
        Repository<Account> repo = new Repository<>();

        // save one by one, then several at once
        repo.save(new SavingsAccount("Asha Patel", 12000, 500));
        int saved = repo.saveAll(new CurrentAccount("Ravi Shah", 3500, 2000),
                                 new FixedDepositAccount("Meena Joshi", 50000),
                                 new SavingsAccount("Kiran Desai", 800, 500));
        System.out.println("saveAll saved " + saved + " accounts");

        // fetch one by id
        Account found = repo.findById("AC0002");
        System.out.println("findById(AC0002) -> " + found);
        System.out.println("findById(AC9999) -> " + repo.findById("AC9999"));

        // print all
        System.out.println("findAll:");
        for (Account a : repo.findAll()) {
            System.out.println("  " + a);
        }

        // delete
        System.out.println("delete(AC0004) -> " + repo.delete("AC0004") + ", delete again -> " + repo.delete("AC0004"));
        System.out.println("accounts left: " + repo.findAll().size());

        // the same repository class works for another type, because Customer has an @Id field too
        Repository<Customer> customers = new Repository<>();
        customers.save(new Customer("Asha Patel", "asha@example.com", "9876543210"));
        customers.save(new Customer("Ravi Shah", "ravi@example.com", "9123456780"));
        System.out.println("Repository<Customer>.findById(CUST102) -> " + customers.findById("CUST102"));

        // generic method with a bounded type parameter
        Integer[] marks = {70, 80, 90};
        Double[] rates = {4.0, 0.0, 7.0};
        Long[] balances = {12000L, 3500L, 50000L};
        System.out.println("average(Integer[]) = " + Repository.average(marks));
        System.out.println("average(Double[])  = " + Repository.average(rates));
        System.out.println("average(Long[])    = " + Repository.average(balances));

        // a type without @Id is rejected
        try {
            new Repository<String>().save("no id here");
        } catch (IllegalArgumentException e) {
            System.out.println("Repository<String>.save -> " + e.getMessage());
        }
    }
}
