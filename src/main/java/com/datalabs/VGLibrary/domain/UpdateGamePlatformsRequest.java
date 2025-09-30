package com.datalabs.VGLibrary.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGamePlatformsRequest {
    @NotEmpty(message = "At lease one platform is required")
    private List<Integer> platformIds;
}
