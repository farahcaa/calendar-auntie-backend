package org.jaga.controllers;

import org.jaga.model.dtos.LoginRequest;
import org.jaga.model.dtos.LoginResponse;
import org.jaga.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Value("${admin.auth.username}")
  private String adminUsername;

  @Value("${admin.auth.password-hash}")
  private String adminPasswordHash; // BCrypt hash

  public AuthController(PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    // 1. Check username
    if (!adminUsername.equals(request.getUsername())) {
      return ResponseEntity.status(401).build();
    }

    // 2. Check password against stored bcrypt hash
    boolean matches = passwordEncoder.matches(request.getPassword(), adminPasswordHash);
    if (!matches) {
      return ResponseEntity.status(401).build();
    }

    // 3. Generate JWT with ADMIN role
    String token = jwtService.generateAdminToken(adminUsername);

    return ResponseEntity.ok(new LoginResponse(token, "ADMIN"));
  }
}
