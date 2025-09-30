package com.datalabs.VGLibrary.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddGameToLibraryRequest {

    @NotNull(message = "Game ID is required")
    private Integer gameId;

    @NotEmpty(message = "At least one platform is required")
    private List<Integer> platformIds;

    private String completionStatus;
}
