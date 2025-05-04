package com.example.product_api.model.dto.auth;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void gettersAndSetters() {
        RegisterRequest req = new RegisterRequest();
        req.setName("Alice");
        req.setEmail("alice@example.com");
        req.setPassword("securePass");

        assertEquals("Alice", req.getName());
        assertEquals("alice@example.com", req.getEmail());
        assertEquals("securePass", req.getPassword());
    }

    @Test
    void equalsAndHashCode_sameProperties_areEqual() {
        RegisterRequest r1 = new RegisterRequest();
        r1.setName("Bob");
        r1.setEmail("bob@example.com");
        r1.setPassword("password1");

        RegisterRequest r2 = new RegisterRequest();
        r2.setName("Bob");
        r2.setEmail("bob@example.com");
        r2.setPassword("password1");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equals_differentProperties_notEqual() {
        RegisterRequest r1 = new RegisterRequest();
        r1.setName("Bob");
        r1.setEmail("bob@example.com");
        r1.setPassword("password1");

        RegisterRequest r2 = new RegisterRequest();
        r2.setName("Carol");
        r2.setEmail("carol@example.com");
        r2.setPassword("password2");

        assertNotEquals(r1, r2);
        assertNotEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void equals_nullAndDifferentClass() {
        RegisterRequest r = new RegisterRequest();
        r.setName("Name");
        r.setEmail("email@example.com");
        r.setPassword("pass1234");

        assertNotEquals(r, null);
        assertNotEquals(r, "some string");
    }

    @Test
    void canEqual_reflection() throws Exception {
        RegisterRequest r1 = new RegisterRequest();
        r1.setName("X");
        r1.setEmail("x@y.com");
        r1.setPassword("pass1234");

        RegisterRequest r2 = new RegisterRequest();
        r2.setName("X");
        r2.setEmail("x@y.com");
        r2.setPassword("pass1234");

        Method canEqual = RegisterRequest.class.getDeclaredMethod("canEqual", Object.class);
        canEqual.setAccessible(true);

        assertTrue((Boolean) canEqual.invoke(r1, r2));
        assertFalse((Boolean) canEqual.invoke(r1, "not a request"));
    }

    @Test
    void toString_containsAllFields() {
        RegisterRequest r = new RegisterRequest();
        r.setName("Dave");
        r.setEmail("dave@example.com");
        r.setPassword("pass1234");

        String s = r.toString();

        assertTrue(s.contains("name=Dave"));
        assertTrue(s.contains("email=dave@example.com"));
        assertTrue(s.contains("password=pass1234"));
    }
}