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

import javax.annotation.processing.Completion;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        return userGameRepository.findByUserId(userId);
    }

    public List<UserGame> getUserGamesByStatus(Integer userId, CompletionStatus status) {
        return userGameRepository.findByUserIdAndCompletionStatus(userId, status);
    }

    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }

    public User loginUser(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    public UserGame addGameToLibrary(Integer userId, AddGameToLibraryRequest request) {
        Optional<UserGame> existingUserGame = userGameRepository.findByUserIdAndGameId(userId, request.getGameId());

        if (existingUserGame.isPresent()) {
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
        Optional<UserGame> optionalUserGame = userGameRepository.findByUserIdAndGameId(userId, gameId);

        if (optionalUserGame.isEmpty()) {
            throw new RuntimeException("User does not own this game");
        }

        UserGame userGame = optionalUserGame.get();

        try {
            CompletionStatus newStatus = CompletionStatus.valueOf(request.getCompletionStatus().toUpperCase());
            userGame.setCompletionStatus(newStatus);
            return userGameRepository.save(userGame);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid completion status: " + request.getCompletionStatus());
        }
    }

    public void deleteGameFromCollection(Integer userId, Integer gameId) {
        Optional<UserGame> optionalUserGame = userGameRepository.findByUserIdAndGameId(userId, gameId);

        if (optionalUserGame.isEmpty()) {
            throw new RuntimeException("User does not own this game");
        }

        List<UserGamePlatform> platformEntries = userGamePlatformRepository.findByUserIdAndGameId(userId, gameId);

        for (UserGamePlatform entry : platformEntries) {
            userGamePlatformRepository.delete(entry);
        }

        userGameRepository.delete(optionalUserGame.get());
    }

    public List<UserGamePlatform> updateGamePlatforms(Integer userId, Integer gameId, UpdateGamePlatformsRequest request) {
        Optional<UserGame> optionalUserGame = userGameRepository.findByUserIdAndGameId(userId, gameId);

        if (optionalUserGame.isEmpty()) {
            throw new RuntimeException("User does not own this game");
        }

        List<UserGamePlatform> existingPlatforms = userGamePlatformRepository.findByUserIdAndGameId(userId, gameId);

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
        List<UserGamePlatform> platformEntries = userGamePlatformRepository.findByUserIdAndPlatformId(userId, platformId);

        if (platformEntries.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserGame> userGames = new ArrayList<>();
        for (UserGamePlatform platformEntry : platformEntries) {
            Optional<UserGame> userGame = userGameRepository.findByUserIdAndGameId(userId, platformEntry.getGameId());
            userGame.ifPresent(userGames::add);
        }

        return userGames;
    }

    public List<UserGamePlatform> getUserGamePlatforms(Integer userId, Integer gameId) {
        return userGamePlatformRepository.findByUserIdAndGameId(userId, gameId);
    }
}

