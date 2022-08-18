package com.example.statussvc.wire.type;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Used to identify availability of the Host via REST.
 */
public enum StatusRest {

    /**
     * Resource is available and responsive.
     */
    ACTIVE,

    /**
     * Resource is unavailable and/or unresponsive.
     */
    INACTIVE,

    /**
     * Not defined resource availability.
     */
    UNKNOWN;

    private final String value = super.toString().toLowerCase();

    @JsonValue
    @Override
    public String toString() {
        return value;
    }

}
