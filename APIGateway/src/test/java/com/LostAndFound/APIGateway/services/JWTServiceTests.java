package com.LostAndFound.APIGateway.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTests {

    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
    }

    @Test
    void testGenerateToken() {
        String phoneNumber = "1234567890";
        String token = jwtService.generateToken(phoneNumber);
        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
    }

    @Test
    void testGeneratedTokenContainsCorrectClaims() {
        String phoneNumber = "1234567890";
        String token = jwtService.generateToken(phoneNumber);
        Claims claims = Jwts.parser()
                .setSigningKey(jwtService.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(phoneNumber, claims.getSubject(), "Token subject should match the phone number");
        assertTrue(claims.getIssuedAt().before(new Date()), "IssuedAt date should be before the current time");
        assertTrue(claims.getExpiration().after(new Date()), "Expiration date should be after the current time");
    }

    @Test
    void testGeneratedTokenIsValidFor30Minutes() {
        // Arrange
        String phoneNumber = "1234567890";

        // Act
        String token = jwtService.generateToken(phoneNumber);
        Claims claims = Jwts.parser()
                .setSigningKey(jwtService.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Assert
        long validity = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();
        assertEquals(30 * 60 * 1000, validity, "Token validity should be 30 minutes");
    }
}
