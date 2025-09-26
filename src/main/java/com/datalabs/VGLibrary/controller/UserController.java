package com.datalabs.VGLibrary.controller;

import com.datalabs.VGLibrary.entity.CompletionStatus;
import com.datalabs.VGLibrary.entity.User;
import com.datalabs.VGLibrary.entity.UserGame;
import com.datalabs.VGLibrary.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
