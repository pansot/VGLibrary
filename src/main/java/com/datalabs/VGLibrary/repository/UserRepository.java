package com.datalabs.VGLibrary.repository;

import com.datalabs.VGLibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    //Find user by username
    Optional<User> findByUsername(String username);

    //Find user by email
    Optional<User> findByEmail(String email);

    //Check if username exists
    boolean existsByUsername(String username);

    //Check if email exists
    boolean existsByEmail(String email);
}
