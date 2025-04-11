package org.example.binarytreeweb.unit.security;

import org.example.binarytreeweb.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    @Mock
    private UserDetails userDetails;

    private JwtService jwtService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();

        // Use reflection to set private fields
        Field secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, "testSecretKey12345678901234567890123456789012");

        Field expirationTimeField = JwtService.class.getDeclaredField("expirationTime");
        expirationTimeField.setAccessible(true);
        expirationTimeField.set(jwtService, 3600000); // 1 hour
    }

    @Test
    void testExtractUsername() {
        when(userDetails.getUsername()).thenReturn("user@example.com");
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);
        assertEquals("user@example.com", username);
    }

    @Test
    void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("user@example.com");

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("user@example.com");

        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenExpired() throws Exception {
        when(userDetails.getUsername()).thenReturn("user@example.com");
        String expiredToken = jwtService.generateToken(userDetails);

        // Use reflection to invoke the private method
        Method isTokenExpiredMethod = JwtService.class.getDeclaredMethod("isTokenExpired", String.class);
        isTokenExpiredMethod.setAccessible(true);
        boolean isExpired = (boolean) isTokenExpiredMethod.invoke(jwtService, expiredToken);

        assertFalse(isExpired); // Adjust based on the test scenario
    }
}