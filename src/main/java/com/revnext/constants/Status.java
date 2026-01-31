package com.revnext.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    // Static method to get the corresponding enum from a string value
    public static Status fromValue(String value) {
        for (Status category : Status.values()) {
            if (category.getValue().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}

