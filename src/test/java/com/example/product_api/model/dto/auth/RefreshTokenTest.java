package com.example.product_api.model.domain;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {

    @Test
    void gettersAndSetters() {
        com.example.product_api.model.domain.RefreshToken token = new com.example.product_api.model.domain.RefreshToken();
        Long id = 42L;
        User user = new User(); user.setId(7L);
        String value = "tokenValue";
        Instant expiry = Instant.now().plusSeconds(3600);

        token.setId(id);
        token.setUser(user);
        token.setToken(value);
        token.setExpiryDate(expiry);

        assertEquals(id, token.getId());
        assertEquals(user, token.getUser());
        assertEquals(value, token.getToken());
        assertEquals(expiry, token.getExpiryDate());
    }

    @Test
    void equalsAndHashCode_sameProperties_areEqual() {
        com.example.product_api.model.domain.RefreshToken t1 = new com.example.product_api.model.domain.RefreshToken();
        com.example.product_api.model.domain.RefreshToken t2 = new com.example.product_api.model.domain.RefreshToken();
        User user = new User(); user.setId(7L);
        Instant expiry = Instant.parse("2025-05-03T00:00:00Z");

        t1.setId(1L);
        t1.setUser(user);
        t1.setToken("abc");
        t1.setExpiryDate(expiry);

        t2.setId(1L);
        t2.setUser(user);
        t2.setToken("abc");
        t2.setExpiryDate(expiry);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void equals_differentProperties_notEqual() {
        com.example.product_api.model.domain.RefreshToken t1 = new com.example.product_api.model.domain.RefreshToken();
        com.example.product_api.model.domain.RefreshToken t2 = new com.example.product_api.model.domain.RefreshToken();
        User user1 = new User(); user1.setId(7L);
        User user2 = new User(); user2.setId(8L);
        Instant expiry1 = Instant.parse("2025-05-03T00:00:00Z");
        Instant expiry2 = Instant.parse("2025-05-04T00:00:00Z");

        t1.setId(1L);
        t1.setUser(user1);
        t1.setToken("abc");
        t1.setExpiryDate(expiry1);

        t2.setId(2L);
        t2.setUser(user2);
        t2.setToken("xyz");
        t2.setExpiryDate(expiry2);

        assertNotEquals(t1, t2);
        assertNotEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void equals_nullAndDifferentClass() {
        com.example.product_api.model.domain.RefreshToken t = new com.example.product_api.model.domain.RefreshToken();
        t.setId(1L);
        t.setUser(new User());
        t.setToken("v");
        t.setExpiryDate(Instant.now());

        assertNotEquals(t, null);
        assertNotEquals(t, "some string");
    }

    @Test
    void canEqual_reflection() throws Exception {
        com.example.product_api.model.domain.RefreshToken t1 = new com.example.product_api.model.domain.RefreshToken();
        com.example.product_api.model.domain.RefreshToken t2 = new com.example.product_api.model.domain.RefreshToken();
        String other = "not a token";

        Method canEqual = com.example.product_api.model.domain.RefreshToken.class.getDeclaredMethod("canEqual", Object.class);
        canEqual.setAccessible(true);

        assertTrue((Boolean) canEqual.invoke(t1, t2));
        assertFalse((Boolean) canEqual.invoke(t1, other));
    }

    @Test
    void toString_containsFields() {
        com.example.product_api.model.domain.RefreshToken t = new com.example.product_api.model.domain.RefreshToken();
        t.setId(5L);
        User user = new User(); user.setId(9L);
        t.setUser(user);
        t.setToken("tok");
        Instant expiry = Instant.parse("2025-05-03T12:00:00Z");
        t.setExpiryDate(expiry);

        String s = t.toString();
        assertTrue(s.contains("id=5"));
        assertTrue(s.contains("user="));
        assertTrue(s.contains("token=tok"));
        assertTrue(s.contains("expiryDate="));
    }
}