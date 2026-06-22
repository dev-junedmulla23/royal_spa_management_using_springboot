package com.example.crm.repository;

import com.example.crm.entity.SequenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepository extends JpaRepository<SequenceEntity, String> {
}

