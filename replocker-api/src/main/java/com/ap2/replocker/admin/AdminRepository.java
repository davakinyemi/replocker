package com.ap2.replocker.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    @Query(name = AdminConstants.FIND_ADMIN_BY_EMAIL)
    Optional<Admin> findByEmail(@Param("email") String adminEmail);
}
