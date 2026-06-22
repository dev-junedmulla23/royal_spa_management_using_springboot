package com.example.crm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;
@Entity
@Table(name = "privileges")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Privilege {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID privilegeId;

    private String readPermission;
    private String deletePermission;
    private String updatePermission;
    private String writePermission;

    @OneToOne(mappedBy = "privilege")
    @JsonBackReference
    private Permission permission;

}