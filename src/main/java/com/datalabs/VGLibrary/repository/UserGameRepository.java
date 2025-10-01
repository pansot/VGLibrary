package com.datalabs.VGLibrary.repository;

import com.datalabs.VGLibrary.entity.CompletionStatus;
import com.datalabs.VGLibrary.entity.UserGame;
import com.datalabs.VGLibrary.entity.UserGameId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.processing.Completion;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, UserGameId> {

    //Gets all games for a user
    List<UserGame> findByUserId(Integer userId);

    //Finds a specific user-game entry
    Optional<UserGame> findByUserIdAndGameId(Integer userId, Integer gameId);

    //Filters by user and status
    List<UserGame> findByUserIdAndCompletionStatus(Integer userId, CompletionStatus completionStatus);
}
