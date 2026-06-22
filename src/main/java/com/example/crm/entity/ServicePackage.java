package com.example.crm.entity;

import com.example.crm.audit.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "service_packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicePackage extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String packageName;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    // duration in days (validity)
    private Integer validityInDays;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // relation with appointment
    @OneToMany(mappedBy = "selectedPackage")
    private List<Appointment> appointments;

    @ManyToMany
    @JoinTable(
            name = "package_services",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<SpaService> services;
}