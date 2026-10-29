public class MiniBank {

    public static void main(String[] args) {
        Account a = new Account("Kiran Desai", 3000);
        Account sameAccount = a;                          // another name for the same object
        Account other = new Account("Kiran Desai", 3000); // different object, different number

        // toString()
        System.out.println(a);
        System.out.println(other);

        // equals() / hashCode()
        System.out.println("a.equals(sameAccount) = " + a.equals(sameAccount));
        System.out.println("a.equals(other)       = " + a.equals(other));
        System.out.println("same hashCode         = " + (a.hashCode() == sameAccount.hashCode()));

        // instanceof
        Object o = a;
        System.out.println("o instanceof Account  = " + (o instanceof Account));
        System.out.println("o instanceof Customer = " + (o instanceof Customer));

        // nested Address + clone()
        Customer c = new Customer("Neha Mehta", "neha@example.com", "9988776655");
        c.setAddress(new Customer.Address("12 Alkapuri", "Vadodara", "390007"));
        Customer copy = c.clone();
        System.out.println("original : " + c + " | " + c.getAddress());
        System.out.println("clone    : " + copy + " | " + copy.getAddress());
        System.out.println("different objects: " + (c != copy)
                + ", address copied too: " + (c.getAddress() != copy.getAddress()));
    }
}
