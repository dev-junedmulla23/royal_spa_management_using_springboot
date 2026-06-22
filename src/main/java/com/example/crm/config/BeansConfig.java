package com.example.crm.config;


import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class BeansConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public GoogleAuthenticator getGoogleAuthenticator(){
        return new GoogleAuthenticator();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        //this is for production (allow only real domains, comment for testing)
//        config.setAllowedOrigins(List.of("https://your-domain.com"));
//        config.setAllowCredentials(true);

        //this is for testing only (allow all origins, comment this for production)
         config.setAllowedOriginPatterns(List.of("*"));
         config.setAllowCredentials(true);

        //this is unchangeable (necessary for both)
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }


//    @Bean
//    public Cloudinary cloudinary() {
//        return new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "YOUR_CLOUD_NAME",   // from dashboard
//                "api_key", "YOUR_API_KEY",       // from dashboard
//                "api_secret", "YOUR_API_SECRET"  // from dashboard
//        ));
//    }
}

