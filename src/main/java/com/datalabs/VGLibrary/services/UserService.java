package com.datalabs.VGLibrary.services;

import com.datalabs.VGLibrary.entity.CompletionStatus;
import com.datalabs.VGLibrary.entity.User;
import com.datalabs.VGLibrary.entity.UserGame;
import com.datalabs.VGLibrary.repository.UserGameRepository;
import com.datalabs.VGLibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGameRepository userGameRepository;

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
}

