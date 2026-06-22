package com.example.crm.service.impl;

import com.example.crm.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    @Override
    public void sendMail(String email, String subject, String resetLink){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(
                "Dear User,\n\n" +
                        "We received a request to reset your account password.\n" +
                        "Please click the link below to proceed:\n\n" +
                        resetLink + "\n\n" +
                        "If you did not initiate this request, no action is required.\n\n" +
                        "Regards,\n" +
                        "Support Team"
        );
        mailSender.send(mailMessage);
    }
}

