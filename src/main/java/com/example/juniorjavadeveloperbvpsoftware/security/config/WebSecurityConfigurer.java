package com.example.juniorjavadeveloperbvpsoftware.security.config;

import com.example.juniorjavadeveloperbvpsoftware.security.filters.JwtAuthenticationFilter;
import com.example.juniorjavadeveloperbvpsoftware.security.util.JwtRequestFilter;
import com.example.juniorjavadeveloperbvpsoftware.security.util.JwtTokenGenerator;
import com.example.juniorjavadeveloperbvpsoftware.security.util.JwtTokenProvider;
import com.example.juniorjavadeveloperbvpsoftware.security.util.UserDetailsExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfigurer {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    public WebSecurityConfigurer(
            JwtTokenGenerator jwtTokenGenerator,
            AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler
    ) {
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Autowired
    void configureAuthenticationManager(
            AuthenticationManagerBuilder auth,
            JwtTokenProvider jwtTokenProvider
    ) {
        auth.authenticationProvider(jwtTokenProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request.requestMatchers("/api/auth/signup",
                                "/api/auth/login",
                                "/ping",
                                "/api/auth/email-confirm/{token}",
                                "/api/auth/change-password",
                                "/api/auth/send/reset-password-email/{email}",
                                "/api/auth/resend/email-confirmation/{email}",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/favicon.ico")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout.deleteCookies("JSESSIONID"))
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtTokenGenerator))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));

        http.addFilterBefore(jwtRequestFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}
