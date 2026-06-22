package com.example.crm.entity;

import com.example.crm.audit.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String bloodGroup;

    @Column(name = "mobile_number", unique = true, nullable = false)
    private String mobileNumber;

    private String address;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_visit_date")
    private LocalDate lastVisitDate;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    // Add this field inside your existing Customer.java entity
    @Column(name = "fcm_token", length = 500)
    private String fcmToken;  // Firebase Cloud Messaging device token
}
