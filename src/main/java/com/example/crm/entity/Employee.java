package com.example.crm.entity;


import com.example.crm.audit.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser appUser;

    @Column(nullable = false)
    private String name;

    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;

    private String email;

    private String specialization;

    private Integer experience;
    @Column(name = "is_available")

    private Boolean isAvailable = true;
    @Column(name = "is_active")

    private Boolean isActive = true;

    @OneToMany(mappedBy = "assignedEmployee")
    private List<Appointment> appointments;
}