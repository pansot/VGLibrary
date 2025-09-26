package com.datalabs.VGLibrary.repository;

import com.datalabs.VGLibrary.entity.CompletionStatus;
import com.datalabs.VGLibrary.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    List<Game> findByTitleContainingIgnoreCase(String title);
    List<Game> findByReleaseYear(Integer year);
    List<Game> findByDeveloper(String developer);
    List<Game> findByPublisher(String publisher);
    Optional<Game> findByTitle(String title);

    List<Game> findByGenres_Name(String genreName);
}
