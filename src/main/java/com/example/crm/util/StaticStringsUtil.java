package com.example.crm.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticStringsUtil {

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${server.port}")
    private String port;
    public String getForgetPasswordLink(String token){
        return baseUrl + ":" + port + "/reset-password?token=" + token;
    }

    public static String getQrCodeUrl(String username, String secretKey){
        return "otpauth://totp/YODDHA:" + username
                + "?secret=" + secretKey
                + "&issuer=YODDHA";
    }
}
