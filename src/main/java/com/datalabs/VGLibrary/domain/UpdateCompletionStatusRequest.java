package com.datalabs.VGLibrary.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompletionStatusRequest {
    @NotBlank(message = "Completion status is required")
    private String completionStatus;
}
