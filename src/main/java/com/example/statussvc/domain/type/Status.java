package com.example.statussvc.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Used to identify availability of the Host.
 */
public enum Status {

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
