package com.example.SecurityUsingMySQLUsingGrok.controller;


import com.example.SecurityUsingMySQLUsingGrok.model.User;
import com.example.SecurityUsingMySQLUsingGrok.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder,
                          EntityManager entityManager) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        // Check if username already exists
        Long userCount = entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", registerRequest.getUsername())
                .getSingleResult();

        if (userCount > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already exists. Please login.");
        }

        Set<String> roles = new HashSet<>();
        // If no roles provided, default to USER
        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            roles.add("ROLE_USER");
        } else {
            // Prefix roles with "ROLE_" if not already present
            for (String role : registerRequest.getRoles()) {
                if (!role.startsWith("ROLE_")) {
                    roles.add("ROLE_"+role);
                } else {
                    roles.add(role);
                }
            }
        }

        User user = new User(
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                roles
        );

        entityManager.persist(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // Exception handler for better error handling
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
    }
}

class AuthRequest {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class RegisterRequest {
    private String username;
    private String password;
    private Set<String> roles;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}