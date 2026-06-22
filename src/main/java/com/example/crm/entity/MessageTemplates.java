package com.example.crm.entity;

/**
 * Centralized message templates for WhatsApp.
 * All messages are in one place — easy to update or localize.
 */
public final class MessageTemplates {

    private MessageTemplates() {}  // Utility class — no instances

    // 🎂 Birthday message
    public static String birthday(String customerName) {
        return String.format(
                "🎉 Happy Birthday, %s!\n\n" +
                        "The entire Royal Spa family wishes you a wonderful day filled with joy! 🌸\n\n" +
                        "🎁 *Special Birthday Gift:* Enjoy 20%% off on any spa service today!\n\n" +
                        "📞 Book your appointment: +91-XXXXXXXXXX\n" +
                        "📍 Royal Spa Centre, Your City",
                customerName
        );
    }

    // ⏰ Appointment reminder
    public static String appointmentReminder(String customerName, String time) {
        return String.format(
                "⏰ *Appointment Reminder*\n\n" +
                        "Hi %s! 👋\n\n" +
                        "This is a reminder that your spa appointment is scheduled at *%s* today.\n\n" +
                        "Please arrive 5 minutes early. See you soon! 💆\n\n" +
                        "📞 Questions? Call: +91-XXXXXXXXXX\n" +
                        "📍 Royal Spa Centre",
                customerName, time
        );
    }

    // 🎁 Promotional offer
    public static String specialOffer(String customerName, String offerDetails) {
        return String.format(
                "🌟 *Exclusive Offer for You, %s!*\n\n" +
                        "%s\n\n" +
                        "⏳ *Limited Time Only* — Don't miss out!\n\n" +
                        "📞 Book now: +91-XXXXXXXXXX\n" +
                        "📍 Royal Spa Centre",
                customerName, offerDetails
        );
    }

    // 📋 Generic custom message
    public static String custom(String customerName, String body) {
        return String.format(
                "Hi %s! 👋\n\n%s\n\n— Royal Spa Centre Team 🌿",
                customerName, body
        );
    }
}