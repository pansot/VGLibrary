package com.datalabs.VGLibrary.services;

import com.datalabs.VGLibrary.domain.*;
import com.datalabs.VGLibrary.entity.CompletionStatus;
import com.datalabs.VGLibrary.entity.User;
import com.datalabs.VGLibrary.entity.UserGame;
import com.datalabs.VGLibrary.entity.UserGamePlatform;
import com.datalabs.VGLibrary.repository.UserGamePlatformRepository;
import com.datalabs.VGLibrary.repository.UserGameRepository;
import com.datalabs.VGLibrary.repository.UserRepository;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGameRepository userGameRepository;

    @Autowired
    private UserGamePlatformRepository userGamePlatformRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserGame> getUserGames(Integer userId) {
        return userGameRepository.findAll().stream()
                .filter(userGame -> userGame.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<UserGame> getUserGamesByStatus(Integer userId, CompletionStatus status) {
        return getUserGames(userId).stream()
                .filter(userGame -> userGame.getCompletionStatus() == status)
                .collect(Collectors.toList());
    }

    public User createUser(CreateUserRequest request) {
        List<User> existingUsers = userRepository.findAll();
        boolean usernameExists = existingUsers.stream()
                .anyMatch(user -> user.getUsername().equals(request.getUsername()));

        if (usernameExists) {
            throw new RuntimeException("Username already exists");
        }

        boolean emailExists = existingUsers.stream()
                .anyMatch(user -> user.getEmail().equals(request.getEmail()));

        if (emailExists) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }

    public User loginUser(LoginRequest request) {
        List<User> allUsers = userRepository.findAll();

        User user = allUsers.stream()
                .filter(u -> u.getUsername().equals(request.getUsername()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if(!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    public UserGame addGameToLibrary(Integer userId, AddGameToLibraryRequest request) {
        List<UserGame> existingUserGames = userGameRepository.findAll().stream()
                .filter(userGames -> userGames.getUserId().equals(userId) && userGames.getGameId().equals(request.getGameId()))
                .collect(Collectors.toList());

        if (!existingUserGames.isEmpty()) {
            throw new RuntimeException("User already owns this game");
        }

        CompletionStatus status = CompletionStatus.NOT_STARTED;
        if (request.getCompletionStatus() != null) {
            try {
                status = CompletionStatus.valueOf(request.getCompletionStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid completion status: " + request.getCompletionStatus());
            }
        }

        UserGame userGame = new UserGame();
        userGame.setUserId(userId);
        userGame.setGameId(request.getGameId());
        userGame.setCompletionStatus(status);
        UserGame savedUserGame = userGameRepository.save(userGame);

        for (Integer platformId : request.getPlatformIds()) {
            UserGamePlatform userGamePlatform = new UserGamePlatform();
            userGamePlatform.setUserId(userId);
            userGamePlatform.setGameId(request.getGameId());
            userGamePlatform.setPlatformId(platformId);
            userGamePlatformRepository.save(userGamePlatform);
        }

        return savedUserGame;
    }

    public UserGame updateCompletionStatus(Integer userId, Integer gameId, UpdateCompletionStatusRequest request) {
        List<UserGame> userGames = userGameRepository.findAll().stream()
                .filter(userGame -> userGame.getUserId().equals(userId) && userGame.getGameId().equals(gameId))
                .collect(Collectors.toList());

        if (userGames.isEmpty()) {
            throw new RuntimeException("User does not own this game");
        }

        UserGame userGame = userGames.get(0);

        try {
            CompletionStatus newStatus = CompletionStatus.valueOf(request.getCompletionStatus().toUpperCase());
            userGame.setCompletionStatus(newStatus);
            return userGameRepository.save(userGame);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid completion status: " + request.getCompletionStatus());
        }
    }

    public void deleteGameFromCollection(Integer userId, Integer gameId) {
        List<UserGame> userGames = userGameRepository.findAll().stream()
                .filter(userGame -> userGame.getUserId().equals(userId) && userGame.getGameId().equals(gameId))
                .collect(Collectors.toList());

        if(userGames.isEmpty()) {
            throw new RuntimeException("User does not own this game");
        }

        List<UserGamePlatform> platformEntries = userGamePlatformRepository.findAll().stream()
                .filter(userGamePlatform -> userGamePlatform.getUserId().equals(userId) && userGamePlatform.getGameId().equals(gameId))
                .collect(Collectors.toList());

        for (UserGamePlatform entry : platformEntries) {
            userGamePlatformRepository.delete(entry);
        }

        UserGame userGame = userGames.get(0);
        userGameRepository.delete(userGame);
    }

    public List<UserGamePlatform> updateGamePlatforms(Integer userId, Integer gameId, UpdateGamePlatformsRequest request) {
        List<UserGame> userGames = userGameRepository.findAll().stream()
                .filter(userGame -> userGame.getUserId().equals(userId) && userGame.getGameId().equals(gameId))
                .collect(Collectors.toList());

        if (userGames.isEmpty()) {
            throw new RuntimeException("User does not own this game");
        }

        List<UserGamePlatform> existingPlatforms = userGamePlatformRepository.findAll().stream()
                .filter(userGamePlatform -> userGamePlatform.getUserId().equals(userId) && userGamePlatform.getGameId().equals(gameId))
                .collect(Collectors.toList());

        for (UserGamePlatform platform : existingPlatforms) {
            userGamePlatformRepository.delete(platform);
        }

        List<UserGamePlatform> newPlatforms = new ArrayList<>();
        for (Integer platformId : request.getPlatformIds()) {
            UserGamePlatform userGamePlatform = new UserGamePlatform();
            userGamePlatform.setUserId(userId);
            userGamePlatform.setGameId(gameId);
            userGamePlatform.setPlatformId(platformId);
            UserGamePlatform saved = userGamePlatformRepository.save(userGamePlatform);
            newPlatforms.add(saved);
        }

        return newPlatforms;
    }

    public List<UserGame> getUserGamesByPlatform(Integer userId, Integer platformId) {
        List<UserGamePlatform> platformEntries = userGamePlatformRepository.findAll().stream()
                .filter(userGamePlatform -> userGamePlatform.getUserId().equals(userId) && userGamePlatform.getPlatformId().equals(platformId))
                .collect(Collectors.toList());

        if (platformEntries.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserGame> userGames = new ArrayList<>();
        for (UserGamePlatform platformEntry : platformEntries) {
            List<UserGame> matchingUserGames = userGameRepository.findAll().stream()
                    .filter(userGame -> userGame.getUserId().equals(userId) && userGame.getGameId().equals(platformEntry.getGameId()))
                    .collect(Collectors.toList());

            userGames.addAll(matchingUserGames);
        }
        return userGames;
    }
}

