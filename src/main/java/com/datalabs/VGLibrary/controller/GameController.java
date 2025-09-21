package com.datalabs.VGLibrary.controller;

import com.datalabs.VGLibrary.entity.Game;
import com.datalabs.VGLibrary.entity.Genre;
import com.datalabs.VGLibrary.entity.Platform;
import com.datalabs.VGLibrary.entity.CompletionStatus;
import com.datalabs.VGLibrary.repository.GameRepository;
import com.datalabs.VGLibrary.repository.GenreRepository;
import com.datalabs.VGLibrary.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PlatformRepository platformRepository;


    @GetMapping("/games")
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @GetMapping("/platforms")
    public List<Platform> getAllPlatforms() {
        return platformRepository.findAll();
    }

    @GetMapping("/games/completed")
    public List<Game> getCompletedGames() {
        List<Game> allGames = gameRepository.findAll();
        return allGames.stream()
                .filter(game -> game.getCompletionStatus() == CompletionStatus.COMPLETED)
                .collect(Collectors.toList());
    }

    @GetMapping("/games/not-started")
    public List<Game> getNotStartedGames() {
        List<Game> allGames = gameRepository.findAll();
        return allGames.stream()
                .filter(game -> game.getCompletionStatus() == CompletionStatus.NOT_STARTED)
                .collect(Collectors.toList());
    }

    @GetMapping("/games/in-progress")
    public List<Game> getInProgressGames() {
        List<Game> allGames = gameRepository.findAll();
        return allGames.stream()
                .filter(game -> game.getCompletionStatus() == CompletionStatus.IN_PROGRESS)
                .collect(Collectors.toList());
    }

    @GetMapping("/games/abandoned")
    public List<Game> getAbandonedGames() {
        List<Game> allGames = gameRepository.findAll();
        return allGames.stream()
                .filter(game -> game.getCompletionStatus() == CompletionStatus.ABANDONED)
                .collect(Collectors.toList());
    }

    @GetMapping("/games/search")
    public List<Game> searchGamesByTitle(@RequestParam String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title);
    }

    @PostMapping("/games")
    public ResponseEntity<Game> createGame(@RequestBody CreateGameRequest request) {
        Game game = new Game();
        game.setTitle(request.getTitle());
        game.setDeveloper(request.getDeveloper());
        game.setPublisher(request.getPublisher());
        game.setReleaseYear(request.getReleaseYear());
        game.setCompletionStatus(CompletionStatus.NOT_STARTED);

        Set<Genre> genres = new HashSet<>();
        for(Integer genreId : request.getGenreIds()) {
            Genre genre = genreRepository.findById(genreId).orElse(null);
            if (genre != null) {
                genres.add(genre);
            }
        }
        game.setGenres(genres);

        Set<Platform> platforms = new HashSet<>();
        for(Integer platformId : request.getPlatformIds()) {
            Platform platform = platformRepository.findById(platformId).orElse(null);
            if(platform != null) {
                platforms.add(platform);
            }
        }
        game.setPlatforms(platforms);

        Game savedGame = gameRepository.save(game);
        return new ResponseEntity<>(savedGame, HttpStatus.CREATED);
    }
}
