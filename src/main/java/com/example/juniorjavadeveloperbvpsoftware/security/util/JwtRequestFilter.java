package com.example.juniorjavadeveloperbvpsoftware.security.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsExtractor userDetailsExtractor;
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    public JwtRequestFilter(UserDetailsExtractor userDetailsExtractor) {
        this.userDetailsExtractor = userDetailsExtractor;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Get Authorization header
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (request.getRequestURI().equals("/api/auth/change-password")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Validate and extract JWT
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            String jwt = authHeader.substring(BEARER_TOKEN_PREFIX.length());
            var userDetailsOpt = userDetailsExtractor.extractFromToken(jwt);

            // Populate SecurityContext
            if (userDetailsOpt.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = userDetailsOpt.get();
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}