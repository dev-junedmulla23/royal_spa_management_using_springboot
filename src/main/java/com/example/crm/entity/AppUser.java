package com.example.crm.entity;

import com.example.crm.audit.Audit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String address;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String email;

    private LocalDate dateOfJoining;
    private double salary;
    private String secretKey;
    private String imageUrl;
    private Boolean multiFactor = false;

    private Boolean isUserLoggedIn;
    private Boolean isDeleted;

    @Column(length = 100000)
    private String qrCodeImage;

    private String resetToken;
    private LocalDateTime tokenExpiryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonIgnore
    private Role role;
}

