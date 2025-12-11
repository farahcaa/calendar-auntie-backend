package org.jaga.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

    Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    return Jwts.builder()
      .setSubject(username)
      .claim("role", "ADMIN")
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(exp))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  // you’ll also want a verify/parse method for your security filter later
}
