package util;

import model.annotation.Audit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Reflective logger: wraps any interface in a dynamic proxy and records every call of a
 * method that carries the @Audit annotation.
 */
public class AuditLogger {
    private final ArrayList<String> trail = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public <T> T wrap(T target, Class<T> type) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, (proxy, method, args) -> {
            boolean audited = method.isAnnotationPresent(Audit.class);
            String call = method.getName() + (args == null ? "[]" : Arrays.toString(args));
            try {
                Object result = method.invoke(target, args);
                if (audited) {
                    record(call + " -> ok");
                }
                return result;
            } catch (InvocationTargetException e) {
                if (audited) {
                    record(call + " -> failed: " + e.getCause().getMessage());
                }
                throw e.getCause();                 // the caller sees the original exception
            }
        });
    }

    private synchronized void record(String entry) {
        trail.add(entry);
    }

    public synchronized ArrayList<String> getTrail() {
        return new ArrayList<>(trail);
    }
}
