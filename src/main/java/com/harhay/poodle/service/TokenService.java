package com.harhay.poodle.service;

import com.harhay.poodle.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.util.stream.Collectors.toList;

@Service
public class TokenService {

    private final String secret = "opdfpodsjfosudfoajfdpai90fuad0fjaoi32";
    private final Long expirationTime = Duration.ofHours(12).getSeconds();

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @SuppressWarnings("unchecked")
    public List<SimpleGrantedAuthority> getAllAuthoritiesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        List<Map<String, String>> rolesMap = claims.get("role", List.class);
        return rolesMap.stream()
            .map(map -> map.get("authority"))
            .map(SimpleGrantedAuthority::new)
            .collect(toList());
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public LocalDateTime getExpirationDateFromToken(String token) {
        return toLocalDateTime(getAllClaimsFromToken(token).getExpiration());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getAuthorities());
        return doGenerateToken(claims, user.getUsername());
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private Claims getAllClaimsFromToken(String token) {
        JwtParser parser = Jwts.parserBuilder().
            setSigningKey(key)
            .build();
        return parser.parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final LocalDateTime expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(now());
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        final LocalDateTime expirationDate = now().plus(expirationTime, HOURS);
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(toDate(now()))
            .setExpiration(toDate(expirationDate))
            .signWith(key)
            .compact();
    }

    public LocalDateTime toLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }

    public Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime
            .atZone(ZoneId.systemDefault())
            .toInstant());
    }
}
