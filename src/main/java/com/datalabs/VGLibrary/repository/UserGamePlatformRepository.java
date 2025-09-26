package com.datalabs.VGLibrary.repository;

import com.datalabs.VGLibrary.entity.UserGamePlatform;
import com.datalabs.VGLibrary.entity.UserGamePlatformId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGamePlatformRepository extends JpaRepository<UserGamePlatform, UserGamePlatformId> {
}
