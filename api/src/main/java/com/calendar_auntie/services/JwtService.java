package com.calendar_auntie.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret; // must be long enough for HS256 key

  @Value("${jwt.expiration-minutes:60}")
  private long expirationMinutes;

  public String generateAdminToken(String username) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(expirationMinutes * 60);

    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    return Jwts.builder()
      .subject(username)
      .claim("role", "ADMIN")
      .issuedAt(Date.from(now))
      .expiration(Date.from(exp))
      .signWith(key, Jwts.SIG.HS256) // NEW way
      .compact();
  }

  public String extractUsername(String token) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    return Jwts.parser()
      .verifyWith(key)           // <--- your secret key
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getSubject();                         // <-- "sub" claim
  }
  public String extractRole(String token) {
    SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .get("role", String.class);
  }

}
