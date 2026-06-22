package com.example.crm.repository;

import com.example.crm.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    List<Appointment> findByAssignedEmployee_Id(UUID id);


    // Count today's appointments
    Long countByAppointmentDate(LocalDate date);

    // Find appointments within next 15 minutes (for reminder scheduler)
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = :today "
            + "AND a.appointmentTime BETWEEN :now AND :fifteenMin "
            + "AND a.status = 'SCHEDULED'")
    List<Appointment> findUpcomingAppointments(
            @Param("today") LocalDate today,
            @Param("now") LocalTime now,
            @Param("fifteenMin") LocalTime fifteenMin);
}
