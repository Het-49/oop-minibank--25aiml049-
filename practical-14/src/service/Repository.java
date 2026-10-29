package service;

import model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generic repository: stores any type T. The key of every object is found by reflection:
 * it is the value of the field marked with @Id.
 */
public class Repository<T> {
    private final HashMap<String, T> items = new HashMap<>();
    private final HashMap<Class<?>, Field> idFields = new HashMap<>();   // reflection result cache

    public T save(T entity) {
        items.put(idOf(entity), entity);
        return entity;
    }

    public T findById(String id) {
        return items.get(id);                  // null when there is no such id
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(items.values());
    }

    public boolean delete(String id) {
        return items.remove(id) != null;
    }

    /** Saves several objects at once; @SafeVarargs is allowed because the method is final. */
    @SafeVarargs
    public final int saveAll(T... entities) {
        for (T entity : entities) {
            save(entity);
        }
        return entities.length;
    }

    /** Generic method with a bounded type parameter: N must be a Number (Integer, Long, Double ...). */
    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        double sum = 0;
        for (N value : values) {
            sum += value.doubleValue();
        }
        return sum / values.length;
    }

    private String idOf(T entity) {
        Field field = idFields.computeIfAbsent(entity.getClass(), Repository::findIdField);
        try {
            return String.valueOf(field.get(entity));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot read the @Id field of " + entity.getClass().getSimpleName(), e);
        }
    }

    private static Field findIdField(Class<?> type) {
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {       // also search the superclasses
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        throw new IllegalArgumentException(type.getSimpleName() + " has no field marked with @Id");
    }
}
