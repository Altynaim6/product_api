package com.example.product_api.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2AuthenticationSuccessHandlerTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler successHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oAuth2User;

    @Captor
    private ArgumentCaptor<Cookie> cookieCaptor;

    private final String redirectUrl = "/api/pages/home";

    @BeforeEach
    void setUp() {
        // no-op
    }

    @Test
    void onAuthenticationSuccess_shouldSetCookieAndRedirect() throws IOException {
        // Arrange
        String email = "test@example.com";
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn(email);

        // Prepare UserDetails passed to JwtService
        ArgumentCaptor<UserDetails> userDetailsCaptor = ArgumentCaptor.forClass(UserDetails.class);
        when(jwtService.generateToken(userDetailsCaptor.capture())).thenReturn("jwt-token");

        // Act
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // Assert: Cookie set
        verify(response).addCookie(cookieCaptor.capture());
        Cookie cookie = cookieCaptor.getValue();
        assertEquals("access_token", cookie.getName());
        assertEquals("jwt-token", cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
        assertEquals(7 * 24 * 60 * 60, cookie.getMaxAge());

        // Assert: Correct UserDetails passed to JwtService
        UserDetails passedDetails = userDetailsCaptor.getValue();
        assertEquals(email, passedDetails.getUsername());
        assertTrue(passedDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));

        // Assert: Redirect performed
        verify(response).sendRedirect(redirectUrl);
    }

    @Test
    void onAuthenticationSuccess_missingEmail_throwsExceptionAndNoRedirect() {
        // Arrange: principal returns OAuth2User without email attribute
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn(null);

        // Act & Assert: expect IllegalArgumentException and no response interaction
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                successHandler.onAuthenticationSuccess(request, response, authentication)
        );
        assertTrue(ex.getMessage().contains("Cannot pass null or empty values"));

        // Ensure no cookie or redirect on failure
        verify(response, never()).addCookie(any(Cookie.class));
    }
}