package com.ecom.inventory.security;

import com.ecom.jwt.blocking.BlockingJwtValidationService;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT Authentication Filter
 * 
 * <p>Spring Security filter that extracts JWT from Authorization header,
 * validates it, and creates JwtAuthenticationToken for Spring Security context.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final BlockingJwtValidationService jwtValidationService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.debug("JWT filter processing request: {} {}", request.getMethod(), request.getRequestURI());
        String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("No Authorization header found for request: {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authorizationHeader.substring(7);
            log.debug("Validating JWT token for request: {} {} (token length: {})", 
                request.getMethod(), request.getRequestURI(), token.length());
            JWTClaimsSet claims = jwtValidationService.validateToken(token);
            
            String userId = jwtValidationService.extractUserId(claims);
            String tenantId = jwtValidationService.extractTenantId(claims);
            List<String> roles = jwtValidationService.extractRoles(claims);

            log.debug("JWT validated successfully: userId={}, tenantId={}, roles={}", userId, tenantId, roles);

            JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                userId, tenantId, roles, token
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (IllegalArgumentException e) {
            log.error("JWT validation failed for request: {} {} - {}", 
                request.getMethod(), request.getRequestURI(), e.getMessage());
            SecurityContextHolder.clearContext();
            // Set 403 response with error message
            try {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String errorMessage = "{\"error\":\"JWT validation failed: " + e.getMessage().replace("\"", "\\\"") + "\"}";
                response.getWriter().write(errorMessage);
                response.getWriter().flush();
            } catch (IOException ioException) {
                log.error("Failed to write error response", ioException);
            }
            return;
        } catch (RuntimeException e) {
            log.error("JWT validation failed for request: {} {} - {}", 
                request.getMethod(), request.getRequestURI(), e.getMessage(), e);
            SecurityContextHolder.clearContext();
            // Set 403 response with error message
            try {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String errorMessage = "{\"error\":\"JWT validation failed: " + e.getMessage().replace("\"", "\\\"") + "\"}";
                response.getWriter().write(errorMessage);
                response.getWriter().flush();
            } catch (IOException ioException) {
                log.error("Failed to write error response", ioException);
            }
            return;
        } catch (Exception e) {
            log.error("Unexpected error during JWT authentication for request: {} {}", 
                request.getMethod(), request.getRequestURI(), e);
            SecurityContextHolder.clearContext();
            // Set 403 response with error message
            try {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                String errorMessage = "{\"error\":\"JWT authentication error: " + e.getMessage().replace("\"", "\\\"") + "\"}";
                response.getWriter().write(errorMessage);
                response.getWriter().flush();
            } catch (IOException ioException) {
                log.error("Failed to write error response", ioException);
            }
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Optional: Clear SecurityContext after request completes
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs");
    }
}

