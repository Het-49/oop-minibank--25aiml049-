package model;

/** A bank customer. Cloneable so a safe copy can be handed out. */
public class Customer implements Cloneable, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /** Nested address type (Practical 3). */
    public static class Address implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private final String line;
        private final String city;
        private final String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine()    { return line; }
        public String getCity()    { return city; }
        public String getPincode() { return pincode; }

        @Override
        public String toString() {
            return line + ", " + city + " - " + pincode;
        }
    }

    private static long customerCounter = 100;

    private final String customerId;
    private String name;
    private String email;
    private String mobile;
    private Address address;

    public Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    private static synchronized String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    public String getCustomerId() { return customerId; }
    public String getName()       { return name; }
    public String getEmail()      { return email; }
    public String getMobile()     { return mobile; }
    public Address getAddress()   { return address; }

    public void setAddress(Address address) {
        this.address = address;
    }

    /** Returns an independent copy (the Address is copied too - a "deep" copy). */
    @Override
    public Customer clone() {
        try {
            Customer copy = (Customer) super.clone();
            if (address != null) {
                copy.address = new Address(address.getLine(), address.getCity(), address.getPincode());
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Customer implements Cloneable", e);
        }
    }

    @Override
    public String toString() {
        return "Customer[" + customerId + ", " + name + ", " + email + ", " + mobile + "]";
    }
}
