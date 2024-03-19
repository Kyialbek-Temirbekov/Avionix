package avia.cloud.flight.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Currency {
    USD("United States Dollar"),
    EUR("Euro"),
    JPY("Japanese Yen"),
    GBP("British Pound Sterling"),
    CHF("Swiss Franc"),
    AUD("Australian Dollar"),
    CAD("Canadian Dollar"),
    CNY("Chinese Yuan"),
    SEK("Swedish Krona"),
    NZD("New Zealand Dollar"),
    KRW("South Korean Won"),
    SGD("Singapore Dollar"),
    HKD("Hong Kong Dollar"),
    NOK("Norwegian Krone"),
    INR("Indian Rupee"),
    KGS("Kyrgyzstani Som"),
    TRY("Turkish Lira"),
    RUB("Russian Ruble");

    private final String fullName;
}

