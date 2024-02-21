package avia.cloud.discovery.util;

import avia.cloud.discovery.entity.enums.Lan;

import java.lang.reflect.Field;
import java.util.List;

public class LanguageUtils {
    public static<T> T getRequired(List<T> items, String requiredLanguage) throws NoSuchFieldException, IllegalAccessException {
        for (T item : items) {
            Field field = item.getClass().getDeclaredField("lan");
            field.setAccessible(true);
            Lan lan = (Lan) field.get(item);
            if (lan.toString().equals(requiredLanguage)) {
                return item;
            }
        }
        return null;
    }
}
