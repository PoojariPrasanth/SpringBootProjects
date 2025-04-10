package com.example.SecurityUsingMySQLUsingGrok.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/user/test")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> userTest() {
        return ResponseEntity.ok("User access granted");
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminTest() {
        return ResponseEntity.ok("Admin access granted");
    }

    @GetMapping("/public/test")
    public ResponseEntity<String> publicTest() {
        return ResponseEntity.ok("Public access granted");
    }
}
