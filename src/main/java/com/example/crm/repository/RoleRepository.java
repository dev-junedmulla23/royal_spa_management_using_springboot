package com.example.crm.repository;

import com.example.crm.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByRoleName(String roleName);
    Optional<Role> findByRoleName(String name);
    List<Role> findByRoleNameContainingIgnoreCase(String name);
    List<Role> findByRoleNameContainingIgnoreCase(String name, Sort sort);
    Page<Role> findByRoleNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByRoleNameAndIdNot(String roleName,UUID id);
}
