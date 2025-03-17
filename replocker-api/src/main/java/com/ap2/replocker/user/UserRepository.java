package com.ap2.replocker.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByHashedEmail(String hashedEmail);
    boolean existsByHashedEmail(String hashedEmail);
}
