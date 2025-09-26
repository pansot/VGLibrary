package com.datalabs.VGLibrary.domain;

import lombok.Data;

import java.util.List;

@Data
public class CreateGameRequest {
    private String title;
    private String developer;
    private String publisher;
    private Integer releaseYear;
    private List<Integer> genreIds;
    private List<Integer> platformIds;
}
