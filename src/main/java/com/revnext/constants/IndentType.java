package com.revnext.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum IndentType {
    NORMAL("Normal"),
    QUARTERLY("Quarterly"),
    YEARLY("Yearly");

    private final String value;

    public static IndentType fromValue(String value) {
        for (IndentType yojanaType : IndentType.values()) {
            if (yojanaType.getValue().equalsIgnoreCase(value)) {
                return yojanaType;
            }
        }
        return null; // No exception, just return null
    }
}

