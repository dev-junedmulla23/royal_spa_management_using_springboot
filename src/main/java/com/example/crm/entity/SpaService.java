package com.example.crm.entity;

import com.example.crm.audit.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "spa_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpaService extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    // duration in minutes
    @Column(nullable = false)
    private Integer duration;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // relation with appointment
    @OneToMany(mappedBy = "spaService")
    private List<Appointment> appointments;

    @ManyToMany(mappedBy = "services")
    private List<ServicePackage> packages;
}