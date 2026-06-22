package com.example.crm.repository;

import com.example.crm.entity.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, UUID>
{

    List<ServicePackage> findByIsActiveTrue();
}
