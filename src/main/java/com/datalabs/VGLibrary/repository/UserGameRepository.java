package com.datalabs.VGLibrary.repository;

import com.datalabs.VGLibrary.entity.UserGame;
import com.datalabs.VGLibrary.entity.UserGameId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGameRepository extends JpaRepository<UserGame, UserGameId> {
}
