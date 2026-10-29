public class MiniBank {

    public static void main(String[] args) {
        // each validator: one correct and one wrong input
        System.out.println("mobile 9876543210  -> " + Validator.isValidMobile("9876543210"));
        System.out.println("mobile 12345       -> " + Validator.isValidMobile("12345"));
        System.out.println("email  a@b.com     -> " + Validator.isValidEmail("a@b.com"));
        System.out.println("email  a@b         -> " + Validator.isValidEmail("a@b"));
        System.out.println("PAN    ABCDE1234F  -> " + Validator.isValidPan("ABCDE1234F"));
        System.out.println("PAN    abcde1234   -> " + Validator.isValidPan("abcde1234"));
        System.out.println("IFSC   SBIN0001234 -> " + Validator.isValidIfsc("SBIN0001234"));
        System.out.println("IFSC   SBIN1001234 -> " + Validator.isValidIfsc("SBIN1001234"));

        // parse a sample command and print its three parts
        Command cmd = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println("type    = " + cmd.type());
        System.out.println("account = " + cmd.accountNumber());
        System.out.println("amount  = " + cmd.amount());

        try {
            CommandParser.parse("DEPOSIT AC0001");
        } catch (IllegalArgumentException e) {
            System.out.println("rejected: " + e.getMessage());
        }

        Account a = new Account("Asha Patel", 7500);
        System.out.println(StatementFormatter.buildStatement(a));
    }
}
