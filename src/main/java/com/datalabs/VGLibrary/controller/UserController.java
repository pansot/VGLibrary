package com.datalabs.VGLibrary.controller;

import com.datalabs.VGLibrary.domain.*;
import com.datalabs.VGLibrary.entity.*;
import com.datalabs.VGLibrary.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8000")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/games")
    public List<UserGame> getUserGames(@PathVariable Integer userId) {
        return userService.getUserGames(userId);
    }

    @GetMapping("/{userId}/games/completed")
    public List<UserGame> getUserCompletedGames(@PathVariable Integer userId) {
        return userService.getUserGamesByStatus(userId, CompletionStatus.COMPLETED);
    }

    @GetMapping("/{userId}/games/in-progress")
    public List<UserGame> getUserInProgressGames(@PathVariable Integer userId) {
        return userService.getUserGamesByStatus(userId, CompletionStatus.IN_PROGRESS);
    }

    @GetMapping("/{userId}/games/not-started")
    public List<UserGame> getUserNotStartedGames(@PathVariable Integer userId) {
        return userService.getUserGamesByStatus(userId, CompletionStatus.NOT_STARTED);
    }

    @GetMapping("/{userId}/games/abandoned")
    public List<UserGame> getUserAbandonedGames(@PathVariable Integer userId) {
        return userService.getUserGamesByStatus(userId, CompletionStatus.ABANDONED);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            User newUser = userService.createUser(request);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
        try {
            User user = userService.loginUser(request);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/{userId}/games")
    public ResponseEntity<?> addGameToLibrary(@PathVariable Integer userId, @Valid @RequestBody AddGameToLibraryRequest request) {
        try {
            UserGame userGame = userService.addGameToLibrary(userId, request);
            return new ResponseEntity<>(userGame, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}/games/{gameId}/completion-status")
    public ResponseEntity<?> updateCompletionStatus(@PathVariable Integer userId, @PathVariable Integer gameId, @Valid @RequestBody UpdateCompletionStatusRequest request) {
        try {
            UserGame updatedUserGame = userService.updateCompletionStatus(userId, gameId, request);
            return new ResponseEntity<>(updatedUserGame, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{userId}/games/{gameId}")
    public ResponseEntity<?> deleteGameFromCollection(@PathVariable Integer userId, @PathVariable Integer gameId) {
        try {
            userService.deleteGameFromCollection(userId, gameId);
            return new ResponseEntity<>("Game removed from collection succesfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}/games/{gameId}/platforms")
    public ResponseEntity<?> updateGamePlatforms(@PathVariable Integer userId, @PathVariable Integer gameId, @Valid @RequestBody UpdateGamePlatformsRequest request) {
        try {
            List<UserGamePlatform> updatedPlatforms = userService.updateGamePlatforms(userId, gameId, request);
            return new ResponseEntity<>(updatedPlatforms, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}/games/platform/{platformId}")
    public List<UserGame> getUserGamesByPlatform(@PathVariable Integer userId, @PathVariable Integer platformId) {
        return userService.getUserGamesByPlatform(userId, platformId);
    }
}
