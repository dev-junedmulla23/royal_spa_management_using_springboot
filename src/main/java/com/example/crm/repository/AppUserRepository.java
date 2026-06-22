package com.example.crm.repository;

import com.example.crm.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    boolean existsByUsername(String username);
    List<AppUser> findByUsernameContainingIgnoreCase(String username);
    boolean existsByRole_Id(UUID roleId);
    boolean existsByEmail(String email);
    boolean existsByUsernameAndIdNot(String username,UUID id);
    Optional<AppUser> getMyUserByUsername(String username);
    Optional<AppUser> findByResetToken(String resetToken);

    @Query("""
        SELECT u FROM AppUser u
        JOIN FETCH u.role
        WHERE u.username = :username
    """)
    Optional<AppUser> getUserWithRole(@Param("username") String username);
}
