import model.Account;
import model.InterestBearing;
import model.Premium;
import model.PremiumSavingsAccount;
import model.SavingsAccount;
import model.CurrentAccount;
import model.Transactable;
import model.WithdrawRule;
import service.BankService;
import util.AnnotationValidator;

import static util.Validator.isValidMobile;       // the one static import

public class MiniBank {

    public static void main(String[] args) {
        Account premium = new PremiumSavingsAccount("Sneha", 20000, 1000);
        Account current = new CurrentAccount("Ravi", 2000, 5000);

        // 1. Transactable: Account implements it
        Transactable t = premium;
        t.deposit(500);
        System.out.println("withdraw 1500 via Transactable -> " + t.withdraw(1500)
                + ", balance = " + premium.getBalance());

        // 2. InterestBearing: default method uses interestRate() and the balance
        InterestBearing ib = premium;
        System.out.println("yearlyInterest = " + ib.yearlyInterest() + " at " + ib.interestRate() + "%");

        // 3. WithdrawRule: anonymous class first, then a lambda
        WithdrawRule viaAnonymous = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return account.getBalance() - amount >= 5000;
            }
        };
        WithdrawRule viaLambda = (account, amount) -> account.getBalance() - amount >= 5000;
        System.out.println("anonymous class: allow(10000) = " + viaAnonymous.allow(premium, 10000));
        System.out.println("lambda         : allow(16000) = " + viaLambda.allow(premium, 16000));

        // 4. Premium marker interface
        System.out.println("premium instanceof Premium = " + (premium instanceof Premium));
        System.out.println("current instanceof Premium = " + (current instanceof Premium));

        // 5. packages + static import + service
        BankService bank = new BankService();
        bank.add(premium);
        bank.add(current);
        bank.add(new SavingsAccount("Asha", 5000, 500));
        System.out.println("total money in bank = Rs." + bank.totalBalance());
        System.out.println("find AC0002 -> " + bank.find("AC0002").getOwnerName());
        System.out.println("isValidMobile(9000000001) = " + isValidMobile("9000000001"));

        // ---- Practical 7: annotations + reflection ----
        System.out.println("---- annotation validation ----");
        Account bad = new SavingsAccount("A very very long customer name that is too long", -250, 0);
        String[] errors = AnnotationValidator.validate(bad);
        System.out.println("errors found: " + errors.length);
        for (String error : errors) {
            System.out.println("  validation error: " + error);
        }
        Account good = new SavingsAccount("Asha", 100, 0);
        System.out.println("errors for a valid account: " + AnnotationValidator.validate(good).length);
    }
}
