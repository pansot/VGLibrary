package com.datalabs.VGLibrary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "developer", length = 100)
    private String developer;

    @Column(name = "publisher", length = 100)
    private String publisher;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "game_genres",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

}
