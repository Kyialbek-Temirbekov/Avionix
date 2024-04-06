package avia.cloud.flight.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Currency {
    USD("United States Dollar", "$"),
    EUR("Euro", "€"),
    JPY("Japanese Yen", "¥"),
    GBP("British Pound Sterling", "£"),
    CHF("Swiss Franc", "CHF"),
    AUD("Australian Dollar", "A$"),
    CAD("Canadian Dollar", "C$"),
    CNY("Chinese Yuan", "¥"),
    SEK("Swedish Krona", "kr"),
    NZD("New Zealand Dollar", "NZ$"),
    KRW("South Korean Won", "₩"),
    SGD("Singapore Dollar", "S$"),
    HKD("Hong Kong Dollar", "HK$"),
    NOK("Norwegian Krone", "kr"),
    INR("Indian Rupee", "₹"),
    KGS("Kyrgyzstani Som", "сом"),
    TRY("Turkish Lira", "₺"),
    RUB("Russian Ruble", "₽");

    private final String fullName;
    private final String sign;
}


