package com.example.crm.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {


    public static final String SECRET =
            "5367566B59703373367639792F423F4528482B4D6251655468576D5A7134743753"
                    + "67566B59703373367639792F423F4528482B4D6251655468576D5A7134743753"
                    + "67566B59703373367639792F423F4528482B4D6251655468576D5A7134743753";

    // ========================= TOKEN EXTRACTION =========================

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ========================= MFA SUPPORT =========================

    /**
     * Read MFA flag from JWT claim.
     * Used inside JwtAuthenticationFilter.
     */
    public Boolean isMfaVerified(String token) {
        Boolean mfa = extractAllClaims(token).get("mfa", Boolean.class);
        return mfa != null && mfa;
    }

    // ========================= TOKEN GENERATION =========================

    /**
     * OLD METHOD (BACKWARD COMPATIBLE)
     * Used in places where MFA is not needed.
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("mfa", false); // default false
        return createToken(claims, username);
    }

    /**
     * NEW INDUSTRY STANDARD METHOD
     * MFA-aware JWT generation
     */
    public String generateToken(String username, boolean mfaVerified) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("mfa", mfaVerified);
        return createToken(claims, username);
    }

    /**
     * Used for password reset token generation
     */
    public String generatePasswordResetToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("pwd_reset", true);
        return createToken(claims, email);
    }

    // ========================= INTERNAL HELPERS =========================

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24) // 24 hours
                )
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateTempToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("mfa_temp", true);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 min
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}

