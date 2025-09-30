package com.datalabs.VGLibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGamePlatformId implements Serializable {
    private Integer userId;
    private Integer gameId;
    private Integer platformId;
}
