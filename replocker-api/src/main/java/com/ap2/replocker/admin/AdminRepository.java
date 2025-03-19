package com.ap2.replocker.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Optional<Admin> findByUsername(@Param("username") String username);
    Optional<Admin> findByEmail(@Param("hashed_email") String email);
    Optional<Admin> findByKeycloakUserId(@Param("keycloak_user_id") UUID keycloakUserId);
    boolean existsByUsername(@Param("username") String username);
    boolean existsByEmail(@Param("hashed_email") String email);
    boolean existsByKeycloakUserId(@Param("keycloak_user_id") UUID keycloakUserId);
}
