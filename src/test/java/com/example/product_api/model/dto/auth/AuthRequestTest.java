package com.example.product_api.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestTest {
    @Test
    void gettersAndSetters() {
        AuthRequest req = new AuthRequest();
        req.setEmail("user@example.com");
        req.setPassword("secret");

        assertEquals("user@example.com", req.getEmail());
        assertEquals("secret", req.getPassword());
    }

    @Test
    void equalsAndHashCode_sameProperties_areEqual() {
        AuthRequest r1 = new AuthRequest();
        r1.setEmail("a@b.com");
        r1.setPassword("pwd");

        AuthRequest r2 = new AuthRequest();
        r2.setEmail("a@b.com");
        r2.setPassword("pwd");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equals_differentProperties_notEqual() {
        AuthRequest r1 = new AuthRequest();
        r1.setEmail("a@b.com");
        r1.setPassword("pwd");

        AuthRequest r2 = new AuthRequest();
        r2.setEmail("x@y.com");
        r2.setPassword("pwd");

        assertNotEquals(r1, r2);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equals_nullAndDifferentClass() {
        AuthRequest r = new AuthRequest();
        r.setEmail("u@e.com");
        r.setPassword("p");

        assertNotEquals(r, null);
        assertNotEquals(r, "some string");
    }

    @Test
    void canEqual_reflection() throws Exception {
        AuthRequest r1 = new AuthRequest();
        AuthRequest r2 = new AuthRequest();
        String text = "foo";

        Method canEqual = AuthRequest.class.getDeclaredMethod("canEqual", Object.class);
        canEqual.setAccessible(true);

        assertTrue((Boolean) canEqual.invoke(r1, r2));
        assertFalse((Boolean) canEqual.invoke(r1, text));
    }

    @Test
    void toString_containsFields() {
        AuthRequest r = new AuthRequest();
        r.setEmail("alpha@beta.com");
        r.setPassword("pass123");
        String s = r.toString();

        assertTrue(s.contains("email=alpha@beta.com"));
        assertTrue(s.contains("password=pass123"));
    }
}