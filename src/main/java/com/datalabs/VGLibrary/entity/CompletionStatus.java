package com.datalabs.VGLibrary.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CompletionStatus {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    ABANDONED("Abandoned");

    private final String displayName;

    CompletionStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
