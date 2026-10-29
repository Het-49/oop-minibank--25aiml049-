package util;

/** Tiny AutoCloseable used for the try-with-resources example. */
public class AuditSession implements AutoCloseable {
    private final String name;

    public AuditSession(String name) {
        this.name = name;
        System.out.println("  [session] opened: " + name);
    }

    public void log(String message) {
        System.out.println("  [session] " + name + " -> " + message);
    }

    @Override
    public void close() {
        System.out.println("  [session] closed: " + name);
    }
}
