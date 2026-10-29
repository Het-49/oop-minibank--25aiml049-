import java.util.Scanner;

public class MiniBank {

    public static void main(String[] args) {
        BankInfo info = new BankInfo("MiniBank", "Vadodara Main Branch");
        System.out.println("=======================================");
        System.out.println("  " + info);
        System.out.println("=======================================");

        Scanner in = new Scanner(System.in);
        MenuOption[] options = MenuOption.values();
        boolean running = true;

        while (running) {
            System.out.println();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.print("Choose an option: ");
            if (!in.hasNextLine()) {
                break;                                  // input ended
            }

            MenuOption option;
            try {
                option = options[Integer.parseInt(in.nextLine().trim()) - 1];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Please enter a number from 1 to " + options.length + ".");
                continue;
            }

            String message = switch (option) {
                case OPEN_ACCOUNT -> "Open Account - to be implemented in a later lab";
                case DEPOSIT      -> "Deposit - to be implemented in a later lab";
                case WITHDRAW     -> "Withdraw - to be implemented in a later lab";
                case TRANSFER     -> "Transfer - to be implemented in a later lab";
                case EXIT         -> "Goodbye! Thank you for using MiniBank.";
            };
            System.out.println(message);

            if (option == MenuOption.EXIT) {
                running = false;
            }
        }
    }
}
