package avia.cloud.flight.util;

import java.math.BigDecimal;
import java.util.Currency;

public class CurrencyUtils {
    public static long convertToSmallestUnit(double amount, Currency currency) {
        int fractionDigits = currency.getDefaultFractionDigits();
        BigDecimal amountInSmallestUnit = BigDecimal.valueOf(amount).movePointRight(fractionDigits);
        return amountInSmallestUnit.longValue();
    }
}
