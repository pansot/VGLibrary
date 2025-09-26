package com.datalabs.VGLibrary.controller;

import com.datalabs.VGLibrary.domain.CreateGameRequest;
import com.datalabs.VGLibrary.entity.Game;
import com.datalabs.VGLibrary.entity.Genre;
import com.datalabs.VGLibrary.entity.Platform;
import com.datalabs.VGLibrary.repository.GameRepository;
import com.datalabs.VGLibrary.repository.GenreRepository;
import com.datalabs.VGLibrary.repository.PlatformRepository;
import com.datalabs.VGLibrary.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private GameService gameService;

    @GetMapping("/games")
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @GetMapping("/platforms")
    public List<Platform> getAllPlatforms() {
        return platformRepository.findAll();
    }

    @GetMapping("/games/search")
    public List<Game> searchGamesByTitle(@RequestParam String title) {
        return gameRepository.findByTitleContainingIgnoreCase(title);
    }

    @PostMapping("/games/create-game")
    public ResponseEntity<Game> createGame(@RequestBody CreateGameRequest request) {
        Game savedGame = gameService.createGame(request);
        return new ResponseEntity<>(savedGame, HttpStatus.CREATED);
    }

}
