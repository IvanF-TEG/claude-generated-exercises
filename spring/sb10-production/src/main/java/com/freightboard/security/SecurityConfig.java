package com.freightboard.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Who may call what. Spring Security runs as a chain of FILTERS in front of every controller: a request that
 * fails a rule never reaches your code.
 *
 * Roles:  SHIPPER posts and manages loads, reads bids and accepts one
 *         CARRIER bids on loads
 *         ADMIN   manages carriers and may delete loads
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    public static final String SHIPPER = "SHIPPER";
    public static final String CARRIER = "CARRIER";
    public static final String ADMIN = "ADMIN";

    /**
     * SB09 step 1a: the rules. They're checked TOP TO BOTTOM and the FIRST match wins, so put specific rules before
     *          general ones. Use requestMatchers(HttpMethod.X, "pattern") and hasRole / hasAnyRole / permitAll.
     *          (In patterns, * matches one path segment and ** matches any number of them.)
     *
     *   anyone, no login  : /api/status, /api/greeting, /api/postcodes/**, /api/conversions/**, /api/quotes/**
     *                       and /h2-console/** (a local development tool)
     *   CARRIER           : POST /api/loads/{id}/bids
     *   SHIPPER or ADMIN  : GET  /api/loads/{id}/bids        (carriers must NOT see each other's prices)
     *   SHIPPER           : POST /api/loads, PUT /api/loads/{id}, POST /api/loads/{id}/cancel, POST /api/bids/{id}/accept
     *   ADMIN             : DELETE /api/loads/{id}, POST /api/carriers
     *   any logged-in user: everything else
     *
     * TODO 1b (SB10): /actuator/health and /actuator/info are public (a load balancer checks health without a
     *          password); every other /actuator/** endpoint is ADMIN only. Add the two rules near the top.
     *
     * The rest of the chain is GIVEN: HTTP Basic login, no sessions (every request sends its credentials), and no
     * CSRF protection. CSRF attacks ride on cookies that a browser sends automatically, and this API uses none.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/status", "/api/greeting", "/api/postcodes/**", "/api/conversions/**",
                                "/api/quotes/**", "/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/loads/*/bids").hasRole(CARRIER)
                        .requestMatchers(HttpMethod.GET, "/api/loads/*/bids").hasAnyRole(SHIPPER, ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/loads", "/api/loads/*/cancel", "/api/bids/*/accept").hasRole(SHIPPER)
                        .requestMatchers(HttpMethod.PUT, "/api/loads/*").hasRole(SHIPPER)
                        .requestMatchers(HttpMethod.DELETE, "/api/loads/*").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/carriers").hasRole(ADMIN)
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                // the H2 console draws itself in HTML frames, which are blocked by default
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }

    /**
     * SB09 step 2a: passwords are NEVER stored as plain text. Return PasswordEncoderFactories.createDelegatingPasswordEncoder():
     *          it hashes with bcrypt and stores the algorithm's name in front of the hash ("{bcrypt}$2a$10$..."),
     *          so the algorithm can be upgraded later without breaking old passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * SB09 step 2b: the DEMO users, all with the password from freightboard.security.demo-password, ENCODED:
     *   shipper  (SHIPPER)    shipper2 (SHIPPER)    pennine (CARRIER)    dales (CARRIER)    admin (ADMIN)
     * Build each with User.withUsername(...).password(encoder.encode(password)).roles(...).build()
     * and return new InMemoryUserDetailsManager(user1, user2, ...).
     */
    @Bean
    public UserDetailsService users(PasswordEncoder encoder,
                                    @Value("${freightboard.security.demo-password}") String password) {
        String hash = encoder.encode(password);
        return new InMemoryUserDetailsManager(
                User.withUsername("shipper").password(hash).roles(SHIPPER).build(),
                User.withUsername("shipper2").password(hash).roles(SHIPPER).build(),
                User.withUsername("pennine").password(hash).roles(CARRIER).build(),
                User.withUsername("dales").password(hash).roles(CARRIER).build(),
                User.withUsername("admin").password(hash).roles(ADMIN).build());
    }
}
