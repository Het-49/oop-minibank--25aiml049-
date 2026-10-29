public class MiniBank {

    public static void main(String[] args) {
        Account[] accounts = {
            new SavingsAccount("Asha", 10000, 1000),
            new CurrentAccount("Ravi", 2000, 5000),
            new FixedDepositAccount("Meena", 50000)
        };

        for (Account a : accounts) {
            // the version of interestRate()/canWithdraw() is chosen at run time
            System.out.println(a.getClass().getSimpleName()
                    + ": interestRate = " + a.interestRate() + "%"
                    + ", canWithdraw(9500) = " + a.canWithdraw(9500)
                    + ", canWithdraw(6000) = " + a.canWithdraw(6000));

            if (a instanceof SavingsAccount s) {            // pattern instanceof
                System.out.println("   special case: savings minimum balance = " + s.getMinBalance());
            }
        }

        boolean ok = accounts[1].withdraw(6500);            // overdraft: balance becomes -4500
        System.out.println("withdraw 6500 from the current account -> " + ok
                + ", balance = " + accounts[1].getBalance());
        System.out.println("withdraw 100 from the fixed deposit    -> " + accounts[2].withdraw(100));
        System.out.println(StatementFormatter.buildStatement(accounts[0]));
    }
}
