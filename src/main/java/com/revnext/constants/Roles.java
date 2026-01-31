package com.revnext.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Roles {
    CE("Chief Engineer", 0),
    CETW("Chief Engineer Tubewell", 0),
    SE("Superintendent Engineer", 10),
    SETWC("Superintendent Engineer Tubewell Circle", 20),
    WM("Works Manager", 20),
    EE("Executive Engineer Tubewell", 20),
    AE("Assistant Engineer", 30),
    AES("Assistant Engineer Shop", 30),
    AETWD("Assistant Engineer Tubewell", 30),
    JE("Junior Engineer", 40),
    STORE_KEEPER("Store Keeper", 50),
    CASHIER("Cashier", 60);

    private final String value;
    private final int level;

    public static Roles fromValue(String value) {
        for (Roles role : Roles.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role value: " + value);
    }
}
