package avia.cloud.discovery.util;

import avia.cloud.discovery.entity.enums.Lan;

import java.lang.reflect.Field;
import java.util.List;

public class FieldUtils {
    public static<T> T getField(List<T> items, Lan dialect) throws NoSuchFieldException, IllegalAccessException {
        for (T item : items) {
            Field field = item.getClass().getDeclaredField("lan");
            field.setAccessible(true);
            Lan lan = (Lan) field.get(item);
            if (lan.equals(dialect)) {
                return item;
            }
        }
        return null;
    }
}
