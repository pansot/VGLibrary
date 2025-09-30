package com.datalabs.VGLibrary.repository;

import com.datalabs.VGLibrary.entity.UserGamePlatform;
import com.datalabs.VGLibrary.entity.UserGamePlatformId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGamePlatformRepository extends JpaRepository<UserGamePlatform, UserGamePlatformId> {

    //Find all platforms for a specific user and game
    List<UserGamePlatform> findByUserIdAndGameId(Integer userId, Integer gameId);

    //Find all games a user owns on a specific platform
    List<UserGamePlatform> findByUserIdAndPlatformId(Integer userId, Integer platformId);

    //Find all platform entries for a user
    List<UserGamePlatform> findByUserId(Integer userId);
}
