package com.taskmanager.repository;

import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (for login)
    Optional<User> findByUsername(String username);

    // Check if username already exists (for registration)
    boolean existsByUsername(String username);
}