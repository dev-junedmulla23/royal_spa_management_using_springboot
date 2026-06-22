package com.example.crm.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;  // ✅ CORRECT IMPORT
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TwilioConfig {

    @Value("${twilio.account.sid:}")
    private String accountSid;

    @Value("${twilio.auth.token:}")
    private String authToken;

    @Value("${twilio.whatsapp.number:}")
    private String whatsappNumber;

    @PostConstruct
    public void initTwilio() {

        if (accountSid == null || accountSid.isBlank()) {
            System.out.println("[Twilio] WARNING: Account SID is empty. WhatsApp disabled.");
            return;
        }

        if (authToken == null || authToken.isBlank()) {
            System.out.println("[Twilio] WARNING: Auth Token is empty. WhatsApp disabled.");
            return;
        }

        try {
            Twilio.init(accountSid.trim(), authToken.trim());
            System.out.println("[Twilio] ✅ Initialized successfully! WhatsApp is ENABLED.");
        } catch (Exception e) {
            System.out.println("[Twilio] ❌ Failed: " + e.getMessage());
        }
    }

    public boolean isTwilioEnabled() {
        return accountSid != null && !accountSid.isBlank()
                && authToken != null && !authToken.isBlank();
    }

    public String getWhatsappNumber() {
        return whatsappNumber != null ? whatsappNumber.trim() : "";
    }
}