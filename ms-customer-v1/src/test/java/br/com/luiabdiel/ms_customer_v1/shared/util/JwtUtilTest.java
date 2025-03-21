package br.com.luiabdiel.ms_customer_v1.shared.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String USERNAME = "rob";
    private static final String INVALID_TOKEN = "Invalid token";

    @Test
    void shouldGenerateTokenSuccessfully() {
        String token = JwtUtil.generateToken(USERNAME);

        assertNotNull(token, "Token should not be null");
        assertTrue(token.startsWith("eyJ"), "Token should start with 'eyJ' indicating it's a JWT");
    }

    @Test
    void shouldExtractUsernameFromTokenSuccessfully() {
        String token = JwtUtil.generateToken(USERNAME);

        String extractedUsername = JwtUtil.extractUsername(token);

        assertEquals(USERNAME, extractedUsername, "The extracted username should match the original username");
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        boolean isValid = JwtUtil.validateToken(INVALID_TOKEN);

        assertFalse(isValid, "The invalid token should return false");
    }

    @Test
    void shouldReturnTrueForValidToken() {
        String token = JwtUtil.generateToken(USERNAME);

        boolean isValid = JwtUtil.validateToken(token);

        assertTrue(isValid, "The valid token should return true");
    }

    @Test
    void shouldReturnFalseForExpiredToken() {
        String token = Jwts.builder()
                .setSubject(USERNAME)
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        boolean isValid = JwtUtil.validateToken(token);

        assertFalse(isValid, "The expired token should return false");
    }
}