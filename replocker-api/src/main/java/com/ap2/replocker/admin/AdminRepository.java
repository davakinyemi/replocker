package com.ap2.replocker.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByUsername(@Param("username") String username);
    Optional<Admin> findByHashedEmail(@Param("hashed_email") String hashedEmail);
    Optional<Admin> findByKeycloakUserId(@Param("keycloak_user_id") UUID keycloakUserId);
    boolean existsByUsername(@Param("username") String username);
    boolean existsByHashedEmail(@Param("hashed_email") String hashedEmail);
    boolean existsByKeycloakUserId(@Param("keycloak_user_id") UUID keycloakUserId);
}
