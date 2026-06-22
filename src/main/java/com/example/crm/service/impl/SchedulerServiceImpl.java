package com.example.crm.service.impl;

import com.example.crm.entity.Appointment;
import com.example.crm.entity.Customer;
import com.example.crm.entity.MessageTemplates;
import com.example.crm.repository.AppointmentRepository;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.service.NotificationService;
import com.example.crm.service.WhatsAppService;
import com.example.crm.util.enums.NotificationType;
import com.example.crm.util.enums.WhatsAppMessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerServiceImpl {

    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;   // existing FCM service
    private final WhatsAppService whatsAppService;           // ← NEW: inject WhatsApp

    // ── Birthday Notification ─────────────────────────────────────────
    // Runs every day at 8:00 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendBirthdayNotifications() {
        log.info("[SCHEDULER] 🎂 Running birthday job...");

        LocalDate today = LocalDate.now();
        List<Customer> birthdayCustomers =
                customerRepository.findByBirthdayToday(today.getMonthValue(), today.getDayOfMonth());

        for (Customer customer : birthdayCustomers) {
            try {
                // 1) Send FCM push notification (existing)
                notificationService.sendPushNotification(
                        customer,
                        "🎂 Happy Birthday!",
                        "Enjoy a special birthday discount at Royal Spa!",
                        NotificationType.BIRTHDAY);

                // 2) Send WhatsApp message (NEW) — uses MessageTemplates helper
                String waMessage = MessageTemplates.birthday(customer.getName());
                whatsAppService.sendWhatsAppMessage(
                        customer.getMobileNumber(),
                        waMessage,
                        customer.getId(),
                        WhatsAppMessageType.BIRTHDAY);

                log.info("[SCHEDULER] 🎂 Birthday sent to: {}", customer.getName());

            } catch (Exception e) {
                log.error("[SCHEDULER] Birthday failed for {}: {}", customer.getId(), e.getMessage());
            }
        }
    }

    // ── Appointment Reminder ──────────────────────────────────────────
    // Runs every 15 minutes
    @Scheduled(fixedRate = 900000)
    public void sendAppointmentReminders() {
        log.info("[SCHEDULER] ⏰ Running appointment reminder job...");

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime fifteenMinLater = now.plusMinutes(15);

        List<Appointment> upcoming =
                appointmentRepository.findUpcomingAppointments(today, now, fifteenMinLater);

        for (Appointment appointment : upcoming) {
            try {
                Customer customer = appointment.getCustomer();

                // 1) Send FCM push notification (existing)
                notificationService.sendPushNotification(
                        customer,
                        "⏰ Appointment Reminder",
                        "Your appointment is in 15 minutes!",
                        NotificationType.APPOINTMENT_REMINDER);

                // 2) Send WhatsApp reminder (NEW)
                String waMessage = MessageTemplates.appointmentReminder(
                        customer.getName(),
                        appointment.getAppointmentTime().toString());
                whatsAppService.sendWhatsAppMessage(
                        customer.getMobileNumber(),
                        waMessage,
                        customer.getId(),
                        WhatsAppMessageType.APPOINTMENT_REMINDER);

                log.info("[SCHEDULER] ⏰ Reminder sent for appointment: {}", appointment.getId());

            } catch (Exception e) {
                log.error("[SCHEDULER] Reminder failed for appointment {}: {}",
                        appointment.getId(), e.getMessage());
            }
        }
    }
}
