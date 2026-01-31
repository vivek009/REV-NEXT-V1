package com.revnext.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MachineAvailability {
    WORKING("Working"),
    NOT_WORKING("Not Working");

    private final String displayName;

    public static MachineAvailability fromDisplayName(String displayName) {
        for (MachineAvailability type : MachineAvailability.values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with display name: " + displayName);
    }

}
