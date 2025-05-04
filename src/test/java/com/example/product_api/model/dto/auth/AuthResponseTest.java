package com.example.product_api.model.dto.auth;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void gettersAndSetters() {
        AuthResponse resp = new AuthResponse();
        resp.setAccessToken("access123");
        resp.setRefreshToken("refresh456");

        assertEquals("access123", resp.getAccessToken());
        assertEquals("refresh456", resp.getRefreshToken());
    }

    @Test
    void equalsAndHashCode_sameProperties_areEqual() {
        AuthResponse r1 = new AuthResponse("a1", "r1");
        AuthResponse r2 = new AuthResponse("a1", "r1");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equals_differentProperties_notEqual() {
        AuthResponse r1 = new AuthResponse("a1", "r1");
        AuthResponse r2 = new AuthResponse("a2", "r1");

        assertNotEquals(r1, r2);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equals_nullAndDifferentClass() {
        AuthResponse r = new AuthResponse("a", "r");
        assertNotEquals(r, null);
        assertNotEquals(r, "some string");
    }

    @Test
    void canEqual_reflection() throws Exception {
        AuthResponse r1 = new AuthResponse("x", "y");
        AuthResponse r2 = new AuthResponse("x", "y");

        Method canEqual = AuthResponse.class.getDeclaredMethod("canEqual", Object.class);
        canEqual.setAccessible(true);

        // should be true for same type
        assertTrue((Boolean) canEqual.invoke(r1, r2));
        // false for other type
        assertFalse((Boolean) canEqual.invoke(r1, "not auth response"));
    }

    @Test
    void toString_containsFields() {
        AuthResponse r = new AuthResponse("tokenA", "tokenR");
        String str = r.toString();

        assertTrue(str.contains("accessToken=tokenA"));
        assertTrue(str.contains("refreshToken=tokenR"));
    }
}