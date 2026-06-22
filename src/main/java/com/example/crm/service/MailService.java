package com.example.crm.service;

public interface MailService {
    void sendMail(String email, String Subject, String resetLink);
}
