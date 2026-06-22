package com.example.crm.config.jwt;

import com.example.crm.entity.AppUser;
import com.example.crm.repository.AppUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final MyUserDetailsService userDetailsService;
    private final AppUserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // ===== Extract JWT =====
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtils.extractUsername(token);
            } catch (Exception e) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // ===== Authenticate =====
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (jwtUtils.validateToken(token, userDetails)) {

                // ===== MFA Enforcement =====
                boolean mfaEnabled = isMfaEnabledInDb(username);
                boolean mfaVerified = jwtUtils.isMfaVerified(token);

                String uri = request.getRequestURI();

                boolean mfaBypass =
                        uri.contains("/api/auth/login") ||
                                uri.contains("/api/auth/login-with-otp") ||
                                uri.contains("/api/auth/enable-two-factor-authentication") ||
                                uri.contains("/api/auth/verify-otp") ||
                                uri.contains("/api/auth/disable-two-factor-authentication") ||
                                uri.contains("/api/auth/logout");

                if (mfaEnabled && !mfaVerified && !mfaBypass) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("MFA verification required");
                    return;
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isMfaEnabledInDb(String username) {
        return userRepository.getMyUserByUsername(username)
                .map(AppUser::getMultiFactor)
                .orElse(false);
    }
}
