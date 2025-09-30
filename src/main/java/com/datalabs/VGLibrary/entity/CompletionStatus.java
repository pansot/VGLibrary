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

//    @JsonValue  // This makes JSON responses show "Completed" instead of "COMPLETED"
    public String getDisplayName() {
        return displayName;
    }
}