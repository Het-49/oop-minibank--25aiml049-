package util;

import model.annotation.MaxLength;
import model.annotation.Positive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationValidator {

    /** Checks the @Positive and @MaxLength rules of every field and returns the error messages. */
    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();
        // walk up the superclasses so private fields inherited from Account are checked too
        for (Class<?> c = obj.getClass(); c != null; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class) && value instanceof Number n && n.longValue() <= 0) {
                        String message = field.getAnnotation(Positive.class).message();
                        errors.add(field.getName() + " " + message + " (was " + n + ")");
                    }
                    if (field.isAnnotationPresent(MaxLength.class) && value instanceof String s) {
                        int max = field.getAnnotation(MaxLength.class).value();
                        if (s.length() > max) {
                            errors.add(field.getName() + " length must be <= " + max + " (was " + s.length() + ")");
                        }
                    }
                } catch (IllegalAccessException e) {
                    errors.add(field.getName() + " could not be read: " + e.getMessage());
                }
            }
        }
        return errors.toArray(new String[0]);
    }
}
