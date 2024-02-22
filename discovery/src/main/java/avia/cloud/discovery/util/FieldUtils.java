package avia.cloud.discovery.util;

import avia.cloud.discovery.entity.enums.Lan;

import java.lang.reflect.Field;
import java.util.List;

public class FieldUtils {
    public static<T> T getField(List<T> items, String declaredField, String value) throws NoSuchFieldException, IllegalAccessException {
        for (T item : items) {
            Field field = item.getClass().getDeclaredField(declaredField);
            field.setAccessible(true);
            Lan lan = (Lan) field.get(item);
            if (lan.toString().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
