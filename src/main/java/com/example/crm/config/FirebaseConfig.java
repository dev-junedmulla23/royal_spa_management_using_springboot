package com.example.crm.config;

import org.springframework.beans.factory.annotation.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path}")
    private String serviceAccountPath;

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        // Check if already initialized (important for DevTools hot reload)
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        ClassPathResource resource = new ClassPathResource(serviceAccountPath);
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(resource.getInputStream());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        return FirebaseApp.initializeApp(options);
    }
}