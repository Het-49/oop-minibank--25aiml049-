package util;

import model.Account;

public class StatementFormatter {

    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("==============================\n");
        sb.append("      MINIBANK STATEMENT\n");
        sb.append("==============================\n");
        sb.append(String.format("Account No : %s%n", account.getAccountNumber()));
        sb.append(String.format("Owner      : %s%n", account.getOwnerName()));
        sb.append(String.format("Balance    : Rs.%d%n", account.getBalance()));
        sb.append(String.format("Status     : %s%n", account.isActive() ? "ACTIVE" : "CLOSED"));
        sb.append("==============================");
        return sb.toString();
    }
}
