package com.revnext.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum JWTConstants {
    ROLES("Roles"),
    USER_ID("UserId"),
    WORKSHOP("workshop"),
    EXP("expiredAt"),
    IAT("issuesAt"),
    CLIENT_IP("Client IP"),
    DIVISION_NAME("Division Name"),
    CIRCLE_NAME("Circle Name"),
    SHOP_NAME("Shop Name");;
    private final String value;

    public static JWTConstants fromValue(String value) {
        for (JWTConstants role : JWTConstants.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Invalid Role value: " + value);
    }
}
