package com.ap2.replocker.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    // Optional<Admin> findByUsername(@Param("username") String username);
    // Optional<Admin> findByEmail(@Param("email") String email);

    @Query("SELECT a FROM Admin a WHERE a.keycloakUserId = :keycloak_user_id")
    Optional<Admin> findByKeycloakUserId(@Param("keycloak_user_id") UUID keycloakUserId);

    /* @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Admin a WHERE a.username = :username")
    boolean existsByUsername(@Param("username") String username); */

    /* @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Admin a WHERE a.email = :email")
    boolean existsByEmail(@Param("email") String email); */

    /*
    boolean existsByKeycloakUserId(@Param("keycloak_user_id") UUID keycloakUserId); */
}
