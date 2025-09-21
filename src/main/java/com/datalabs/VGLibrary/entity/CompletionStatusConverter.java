package com.datalabs.VGLibrary.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CompletionStatusConverter implements AttributeConverter<CompletionStatus, String> {

    @Override
    public String convertToDatabaseColumn(CompletionStatus status) {
        if (status == null) {
            return null;
        }
        return status.getDisplayName();
    }

    @Override
    public CompletionStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        for (CompletionStatus status : CompletionStatus.values()) {
            if (status.getDisplayName().equals(dbData)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Unknown completion status: " +dbData);
    }
}
