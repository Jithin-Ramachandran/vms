package com.example.vms.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

//handle JWT token creation and validation.

@Component
public class JwtUtil {

    private final String SECRET_KEY = "secret123Key";
    private final long EXPIRATION_TIME = 1000 * 60 * 60; //1 HOUR

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    //Generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Validate JWT Token
    public boolean validateToken(String token) {
        try{
             Jwts.parserBuilder()
                     .setSigningKey(getSigningKey())
                     .build()
                     .parseClaimsJws(token);
             return true;
        } catch (JwtException e) {
            return false; //invalid token
        }
    }

    //extract username from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}

// Generates JWT tokens using a secret key
// Validates if a given token is valid or expired
// Extracts the username from a token
