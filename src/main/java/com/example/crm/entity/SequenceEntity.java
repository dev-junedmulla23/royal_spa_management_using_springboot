package com.example.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sequences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SequenceEntity {
    @Id
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "value", nullable = false)
    private Long value;
}

