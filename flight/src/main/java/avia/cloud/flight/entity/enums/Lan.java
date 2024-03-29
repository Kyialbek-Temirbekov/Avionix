package avia.cloud.flight.entity.enums;

public enum Lan {
    KY,
    TR,
    EN,
    RU;
    public static Lan of(String lan) {
        return Lan.valueOf(lan.toUpperCase());
    }
}
